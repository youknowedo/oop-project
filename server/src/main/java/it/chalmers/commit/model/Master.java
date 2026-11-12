package it.chalmers.commit.model;

import it.chalmers.commit.persistence.DbClient;

public class Master {
    private DbClient db;
    private Event[] events = new Event[0];
}
