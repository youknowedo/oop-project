package it.chalmers.commit.model;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class Event {
    private String id;
    private User poster;
    private String name;
    private String description;
    private LocalDateTime date;
    private List<Registration> registrations = new ArrayList<>(); //status i regidtration används för att skilja mellan attending och queued.
    private int capacity; 
    private LocalDateTime signUpOpens;
    private EventStatus status;
    
    protected Event() {
    }

    public Event(String name, String description, User poster, LocalDateTime date, int capacity, LocalDateTime signUpOpens) {
        this.id = UUID.randomUUID().toString();
        this.name = name; 
        this.description = description; 
        this.poster = poster; 
        this.date = date;
        this.capacity = capacity;
        this.signUpOpens = signUpOpens;
        this.status = EventStatus.DRAFT;
    }

    public void publish() {
        if (this.status == EventStatus.DRAFT) {
            this.status = EventStatus.PUBLISHED;
        }
    }

    public void cancel () {
        if (this.status == EventStatus.PUBLISHED) {
            this.status = EventStatus.CANCELLED;
        }
    }
    public void signUp(User user, LocalDateTime now) {
        int attending = 0;
        
        for (Registration registration: registrations) {
           
            if (registration.getUser().getId().equals(user.getId())) {
                throw new IllegalStateException("User is already signed up for this event");
            
            }
    
            
            if (registration.getStatus() == RegistrationStatus.ATTENDING) {
                attending ++;
            }
        } 

        if (this.status != EventStatus.PUBLISHED ||
            now.isBefore(this.signUpOpens)) {
                throw new IllegalStateException(" Event is not open for sign up");
        }            
            
        if (attending < this.capacity) {
            Registration registration = new Registration(
                user, 
                RegistrationStatus.ATTENDING, 
                now
            );
        
            registrations.add(registration);

        } else {     
            Registration registration = new Registration(
                    user, 
                    RegistrationStatus.QUEUED, 
                    now 
                );
                registrations.add(registration);

                }
            }
        }  






