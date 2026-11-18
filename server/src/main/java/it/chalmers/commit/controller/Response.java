package it.chalmers.commit.controller;

/** What a handler answers with: a status code and an object that is sent as JSON. */
public record Response(int status, Object body) {

  public static Response ok(Object body) {
    return new Response(200, body);
  }

  public static Response created(Object body) {
    return new Response(201, body);
  }
}
