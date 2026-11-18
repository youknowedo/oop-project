package it.chalmers.commit.controller;

import it.chalmers.commit.view.HealthView;

/** Lets the web app and deployments check that the server is up. */
public class HealthController {

  public void register(Router router) {
    router.get("/api/health", request -> Response.ok(new HealthView("ok")));
  }
}
