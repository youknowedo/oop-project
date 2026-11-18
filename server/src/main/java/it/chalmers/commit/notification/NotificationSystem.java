package it.chalmers.commit.notification;
import java.util.ArrayList;
import java.util.List;

public class NotificationSystem {
    private List<NotificationObserver> observers = new ArrayList<>();

    public void addObserver(NotificationObserver notificationObserver) {
        this.observers.add(notificationObserver);
    }

    public void removeObserver(NotificationObserver notificationObserver) {
        this.observers.remove(notificationObserver);
    }

    public void notifyObserver(String subjectString, String messageString) {
        for (NotificationObserver observer : observers) {
            observer.notify(subjectString, messageString);
        }
    }

}
