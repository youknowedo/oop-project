package it.chalmers.commit;

import it.chalmers.commit.model.Database;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Drafts the next migration from the entity classes. Run it with {@code ./new-migration
 * description}. It brings the local database up to date with the existing migrations, asks
 * Hibernate what SQL is missing to match the entities, and saves that as the next numbered file in
 * {@code db/migration}. Always read the file before committing it: Hibernate sees a renamed column
 * as a drop plus an add.
 */
public class NewMigration {

  private static final Path DIRECTORY = Path.of("src/main/resources/db/migration");
  private static final Pattern NUMBERED = Pattern.compile("^(\\d+)__.*\\.sql$");

  public static void main(String[] args) throws IOException {
    if (args.length != 1 || !args[0].matches("[a-z0-9_]+")) {
      System.err.println("Usage: ./new-migration description_in_snake_case");
      System.exit(1);
    }

    Settings settings = Settings.fromEnvironment();
    Database.migrate(settings.dbUrl(), settings.dbUser(), settings.dbPassword());

    Path draft = Files.createTempFile("schema-diff", ".sql");
    try {
      Database.writeSchemaDiff(settings.dbUrl(), settings.dbUser(), settings.dbPassword(), draft);

      String sql = Files.readString(draft).strip();
      if (sql.isEmpty()) {
        System.out.println("The database already matches the entities, no migration needed.");
        return;
      }

      Files.createDirectories(DIRECTORY);
      Path file = DIRECTORY.resolve(String.format("%03d__%s.sql", nextNumber(), args[0]));
      Files.writeString(file, sql + System.lineSeparator());
      System.out.println("Wrote " + file + ". Read it through before committing.");
    } finally {
      Files.deleteIfExists(draft);
    }
  }

  private static int nextNumber() throws IOException {
    try (Stream<Path> files = Files.list(DIRECTORY)) {
      return files
              .map(file -> NUMBERED.matcher(file.getFileName().toString()))
              .filter(Matcher::matches)
              .mapToInt(matcher -> Integer.parseInt(matcher.group(1)))
              .max()
              .orElse(0)
          + 1;
    }
  }
}
