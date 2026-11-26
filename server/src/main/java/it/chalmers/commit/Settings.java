package it.chalmers.commit;

/** Settings read from environment variables, with defaults for local development. */
public record Settings(int port, String webOrigin, String dbUrl, String dbUser, String dbPassword) {

  public static Settings fromEnvironment() {
    return new Settings(
        Integer.parseInt(env("PORT", "8080")),
        env("WEB_ORIGIN", "http://localhost:5173"),
        env("DB_URL", "jdbc:postgresql://localhost:5432/oop"),
        env("DB_USER", "postgres"),
        env("DB_PASSWORD", "postgres"));
  }

  private static String env(String name, String fallback) {
    String value = System.getenv(name);
    return value == null || value.isBlank() ? fallback : value;
  }
}
