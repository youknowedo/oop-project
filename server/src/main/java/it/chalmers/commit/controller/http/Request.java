package it.chalmers.commit.controller.http;

import java.util.Map;

/**
 * An incoming request: the path parameters (like {@code {id}}), the headers (names in lower case)
 * and the raw body.
 */
public record Request(
    String method, Map<String, String> pathParams, Map<String, String> headers, String body) {

  public String header(String name) {
    return headers.get(name.toLowerCase());
  }

  public String pathParam(String name) {
    return pathParams.get(name);
  }

  /** Parses the body as JSON, answering 400 if it is missing or invalid. */
  public <T> T body(Class<T> type) {
    try {
      return Json.fromJson(body, type);
    } catch (IllegalArgumentException e) {
      throw HttpException.badRequest(e.getMessage());
    }
  }
}
