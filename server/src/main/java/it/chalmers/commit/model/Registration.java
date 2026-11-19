package it.chalmers.commit.model;
import java.time.LocalDateTime;
import java.util.UUID;

public class Registration {
    private String id;
    private RegistrationStatus status;
    private LocalDateTime createdAt; 
    private LocalDateTime cancelledAt;
    private User user; 

    protected Registration() {
    }
    
    public Registration(User user, RegistrationStatus status, LocalDateTime createdAt) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.status = status;
        this.createdAt = createdAt;
        this.cancelledAt = null;
        
    
    }

    public void cancel(LocalDateTime time) {
        if (this.status == RegistrationStatus.CANCELLED) {
            System.err.println("Already cancelled");
            return; 
        }
        this.status = RegistrationStatus.CANCELLED;
        this.cancelledAt = time;
    }

    public void markNoShow() {
        if (this.status == RegistrationStatus.ATTENDING) {
            this.status = RegistrationStatus.NO_SHOW;
        }
    }

    public String getId() {
        return id;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public User getUser(){
        return user;
    }
}

