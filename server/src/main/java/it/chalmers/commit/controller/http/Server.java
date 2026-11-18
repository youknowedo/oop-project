package it.chalmers.commit.controller.http;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/** The HTTP server, built on the JDK's own {@code HttpServer}. */
public class Server {

  private final HttpServer httpServer;

  public Server(int port, Router router) throws IOException {
    httpServer = HttpServer.create(new InetSocketAddress(port), 0);
    httpServer.createContext("/", router);
    httpServer.setExecutor(Executors.newCachedThreadPool());
  }

  public void start() {
    httpServer.start();
  }

  public void stop() {
    httpServer.stop(1);
  }
}
