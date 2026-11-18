package it.chalmers.commit;

import it.chalmers.commit.controller.HealthController;
import it.chalmers.commit.controller.Router;
import it.chalmers.commit.controller.Server;
import it.chalmers.commit.model.Database;
import java.io.IOException;

/** Starts the server. Settings come from environment variables, with local defaults. */
public class Main {

  public static void main(String[] args) throws IOException {
    int port = Integer.parseInt(env("PORT", "8080"));
    String webOrigin = env("WEB_ORIGIN", "http://localhost:5173");

    Database database =
        new Database(
            env("DB_URL", "jdbc:postgresql://localhost:5432/oop"),
            env("DB_USER", "postgres"),
            env("DB_PASSWORD", "postgres"));

    Router router = new Router(webOrigin);
    new HealthController().register(router);

    Server server = new Server(port, router);
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  server.stop();
                  database.close();
                }));
    server.start();
    System.out.println("Listening on http://localhost:" + port);
  }

  private static String env(String name, String fallback) {
    String value = System.getenv(name);
    return value == null || value.isBlank() ? fallback : value;
  }
}
