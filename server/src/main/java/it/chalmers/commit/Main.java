package it.chalmers.commit;

import it.chalmers.commit.controller.CurrentUser;
import it.chalmers.commit.controller.EventController;
import it.chalmers.commit.controller.HealthController;
import it.chalmers.commit.controller.UserController;
import it.chalmers.commit.controller.http.Router;
import it.chalmers.commit.controller.http.Server;
import it.chalmers.commit.model.Database;
import it.chalmers.commit.model.Events;
import it.chalmers.commit.model.User;
import it.chalmers.commit.model.UserRole;
import it.chalmers.commit.model.Users;
import java.io.IOException;
import java.util.List;

/** Starts the server. Settings come from environment variables, see {@link Settings}. */
public class Main {

  public static void main(String[] args) throws IOException {
    Settings settings = Settings.fromEnvironment();

    Database database = new Database(settings.dbUrl(), settings.dbUser(), settings.dbPassword());

    Router router = new Router(settings.webOrigin());
    new HealthController().register(router);

    // TEMPORARY: events and users live in memory and two users exist, until login and persistence
    // are built. Pick one with the X-User-Id header.
    Users users = new Users();
    users.add(new User("organizer", "Olivia Organizer", UserRole.ORGANIZER, List.of()));
    users.add(new User("student", "Sam Student", UserRole.STUDENT, List.of()));
    Events events = new Events();
    CurrentUser currentUser = new CurrentUser(users);
    new EventController(currentUser, events).register(router);
    new UserController(currentUser, events).register(router);

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
