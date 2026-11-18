package it.chalmers.commit.notification;

public interface NotificationObserver {

    public void notify(String subjectString, String messageString);
} 
