package it.chalmers.commit.controller;

import java.util.Map;

/** An incoming request: the path parameters (like {@code {id}}) and the raw body. */
public record Request(String method, Map<String, String> pathParams, String body) {

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
