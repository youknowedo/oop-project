package it.chalmers.commit;

import it.chalmers.commit.controller.HealthController;
import it.chalmers.commit.controller.Router;
import it.chalmers.commit.controller.Server;
import it.chalmers.commit.model.Database;
import java.io.IOException;

/** Starts the server. Settings come from environment variables, see {@link Settings}. */
public class Main {

  public static void main(String[] args) throws IOException {
    Settings settings = Settings.fromEnvironment();

    Database database = new Database(settings.dbUrl(), settings.dbUser(), settings.dbPassword());

    Router router = new Router(settings.webOrigin());
    new HealthController().register(router);

    Server server = new Server(settings.port(), router);
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  server.stop();
                  database.close();
                }));
    server.start();
    System.out.println("Listening on http://localhost:" + settings.port());
  }
}
