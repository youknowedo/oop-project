package it.chalmers.commit.model;

import java.time.LocalDateTime;

public class Event {
    private String id;
    private User poster;
    private String name;
    private String description;
    private LocalDateTime date;
    private Request[] attendees = new Request[0];
    private Request[] queue = new Request[0];
}
