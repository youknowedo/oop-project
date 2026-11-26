package it.chalmers.commit.controller.http;

/** Handles one route. */
@FunctionalInterface
public interface Handler {

  Response handle(Request request) throws Exception;
}
