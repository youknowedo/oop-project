package it.chalmers.commit.controller.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import it.chalmers.commit.view.ErrorView;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Sends each request to the handler registered for its method and path. Paths can contain
 * parameters, like {@code /api/events/{id}}. Also adds the CORS headers, answers preflight requests
 * and turns exceptions into JSON error responses.
 */
public class Router implements HttpHandler {

  private record Route(String method, List<String> segments, Handler handler) {}

  private final String allowedOrigin;
  private final List<Route> routes = new ArrayList<>();

  public Router(String allowedOrigin) {
    this.allowedOrigin = allowedOrigin;
  }

  public void get(String path, Handler handler) {
    add("GET", path, handler);
  }

  public void post(String path, Handler handler) {
    add("POST", path, handler);
  }

  public void put(String path, Handler handler) {
    add("PUT", path, handler);
  }

  public void delete(String path, Handler handler) {
    add("DELETE", path, handler);
  }

  private void add(String method, String path, Handler handler) {
    routes.add(new Route(method, split(path), handler));
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", allowedOrigin);
    exchange.getResponseHeaders().add("Vary", "Origin");

    try (exchange) {
      if (exchange.getRequestMethod().equals("OPTIONS")) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.sendResponseHeaders(204, -1);
        return;
      }
      send(exchange, respond(exchange));
    }
  }

  private Response respond(HttpExchange exchange) throws IOException {
    String method = exchange.getRequestMethod();
    List<String> path = split(exchange.getRequestURI().getPath());
    boolean pathExists = false;

    for (Route route : routes) {
      Optional<Map<String, String>> params = match(route.segments(), path);
      if (params.isEmpty()) {
        continue;
      }
      pathExists = true;
      if (!route.method().equals(method)) {
        continue;
      }
      String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
      try {
        return route.handler().handle(new Request(method, params.get(), body));
      } catch (HttpException e) {
        return error(e.status(), e.getMessage());
      } catch (Exception e) {
        e.printStackTrace();
        return error(500, "Internal server error");
      }
    }
    return pathExists ? error(405, "Method not allowed") : error(404, "Not found");
  }

  private static Response error(int status, String message) {
    return new Response(status, new ErrorView(message));
  }

  private static void send(HttpExchange exchange, Response response) throws IOException {
    byte[] bytes = Json.toJson(response.body()).getBytes(StandardCharsets.UTF_8);
    exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
    exchange.sendResponseHeaders(response.status(), bytes.length);
    try (OutputStream out = exchange.getResponseBody()) {
      out.write(bytes);
    }
  }

  private static List<String> split(String path) {
    return Arrays.stream(path.split("/")).filter(part -> !part.isEmpty()).toList();
  }

  private static Optional<Map<String, String>> match(List<String> pattern, List<String> path) {
    if (pattern.size() != path.size()) {
      return Optional.empty();
    }
    Map<String, String> params = new HashMap<>();
    for (int i = 0; i < pattern.size(); i++) {
      String expected = pattern.get(i);
      if (expected.startsWith("{") && expected.endsWith("}")) {
        params.put(expected.substring(1, expected.length() - 1), path.get(i));
      } else if (!expected.equals(path.get(i))) {
        return Optional.empty();
      }
    }
    return Optional.of(params);
  }
}
