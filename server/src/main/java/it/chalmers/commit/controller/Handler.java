package it.chalmers.commit.controller;

/** Handles one route. */
@FunctionalInterface
public interface Handler {

  Response handle(Request request) throws Exception;
}
