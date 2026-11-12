package it.chalmers.commit.model;

import java.time.LocalDateTime;

public class Event {
    private String id;
    private User poster;
    private String name;
    private String description;
    private LocalDateTime date;
    private Signup[] attendees = new Signup[0];
    private Queue queue = new Queue[0];


    public void cancelEvent() {
        // transition to cancelled
    }

    public void updateEvent() {
        // update description, date etc.

    }

    public void publishEvent() {
        // publish event
    }

    private boolean eventExists() {
        // return true if event already exists
        // prevent same event get published multiple times
    }

    private void startEvent() {
        // do this right when event is ongoing
        // this method should be automatically called by system
        // when the event starting time is past
    }

    public int getAvailableSpots() {
        // return amount of free spots left
        // to show in website
    }

    public boolean canSignup() {
        // if signing up is allowed
        // since there can be last signup time
    }

    public void signUp() {
        // sign up to event
        this.attendees.add(guest)
        this.queue.enqueue(guest)
    }

    public void cancelSignup() {
        // cancel sign up to event
        this.attendees.remove(guest)
        this.queue.dequeue(guest)
    }
}