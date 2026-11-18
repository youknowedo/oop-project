package it.chalmers.commit.model;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * The connection to PostgreSQL. Applies the Flyway migrations on start, then lets Hibernate check
 * that the tables match the entity classes.
 */
public class Database implements AutoCloseable {

  /** Every entity class. Add new entities here so Hibernate knows about them. */
  private static final List<Class<?>> ENTITIES = List.of();

  private final SessionFactory sessionFactory;

  public Database(String url, String user, String password) {
    migrate(url, user, password);

    Configuration configuration = configuration(url, user, password);
    configuration.setProperty("hibernate.hbm2ddl.auto", "validate");
    sessionFactory = configuration.buildSessionFactory();
  }

  /**
   * Applies the migrations in {@code db/migration} that the database has not run yet. Files are
   * named {@code NNN__description.sql}, for example {@code 001__create_tables.sql}.
   */
  public static void migrate(String url, String user, String password) {
    Flyway.configure().dataSource(url, user, password).sqlMigrationPrefix("").load().migrate();
  }

  /**
   * Writes the SQL that would bring the database's current schema in line with the entity classes
   * to {@code target}, without running it. The file is empty if they already match.
   */
  public static void writeSchemaDiff(String url, String user, String password, Path target) {
    Configuration configuration = configuration(url, user, password);
    configuration.setProperty("jakarta.persistence.schema-generation.database.action", "none");
    configuration.setProperty("jakarta.persistence.schema-generation.scripts.action", "update");
    configuration.setProperty(
        "jakarta.persistence.schema-generation.scripts.create-target", target.toString());
    configuration.setProperty("hibernate.hbm2ddl.delimiter", ";");
    configuration.setProperty("hibernate.format_sql", "true");
    configuration.buildSessionFactory().close();
  }

  private static Configuration configuration(String url, String user, String password) {
    Configuration configuration =
        new Configuration()
            .setProperty("jakarta.persistence.jdbc.url", url)
            .setProperty("jakarta.persistence.jdbc.user", user)
            .setProperty("jakarta.persistence.jdbc.password", password);
    ENTITIES.forEach(configuration::addAnnotatedClass);
    return configuration;
  }

  /**
   * Runs the work in one transaction and commits it, or rolls it back if the work throws. Changes
   * made to loaded entities are saved automatically on commit.
   */
  <T> T inTransaction(Function<Session, T> work) {
    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();
      try {
        T result = work.apply(session);
        transaction.commit();
        return result;
      } catch (RuntimeException e) {
        transaction.rollback();
        throw e;
      }
    }
  }

  @Override
  public void close() {
    sessionFactory.close();
  }
}
