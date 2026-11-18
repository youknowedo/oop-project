package it.chalmers.commit.model;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Self-contained event model: Java collections manage attendees and the FIFO queue.
 * TEMPORARY: The nested User, Info and Signup types are compilation placeholders
 * until the team's actual model classes are available.
 *
 * TODO: When integrating the team's User.java, Info.java and Signup.java, remove the
 * nested types below so these names refer to the classes in this package instead.
 * User must provide public String getId(). Signup must provide the constructor
 * public Signup(User user, Event event, Info info), storing all three arguments.
 * No Signup getters, Info methods, custom Queue class or Role enum are required here.
 * Callers currently use Event.User, Event.Info and Event.Signup; update those references
 * to the team's model classes during integration. Adding the files alone is not enough,
 * because nested types take precedence over package-level classes with the same names.
 */
public final class Event {
    /**
     * TEMPORARY compilation placeholder for User.java. Implementations must return a unique,
     * stable, nonblank ID. For a quick example: Event.User guest = () -> "guest-1";
     * TODO fix: Implement public String getId() in the team's package-level User class,
     * then delete this nested interface. Replace Event.User references in callers with User
     * and replace temporary lambdas with actual User objects.
     */
    @FunctionalInterface
    public interface User {
        String getId();
    }

    /**
     * TEMPORARY compilation placeholder for Info.java; Event calls no methods on it.
     * TODO fix: Implement the team's package-level Info class with the agreed signup data,
     * then delete this nested interface. Replace Event.Info references and temporary
     * interface implementations in callers with actual Info objects.
     */
    public interface Info {
    }

    /**
     * TEMPORARY compilation placeholder for Signup.java. This immutable record supplies
     * the constructor and the accessors user(), event(), info().
     * TODO fix: Implement public Signup(User user, Event event, Info info) in the team's
     * package-level Signup class, storing and validating all three arguments. Then delete
     * this nested record and replace Event.Signup references in callers with Signup.
     * If callers use record accessors, adapt them to the team's getters (e.g. getUser()).
     */
    public record Signup(User user, Event event, Info info) {
        public Signup {
            Objects.requireNonNull(user, "User is required");
            Objects.requireNonNull(event, "Event is required");
            Objects.requireNonNull(info, "Signup info is required");
        }
    }

    private final String id;
    private final User poster;
    private String name;
    private String description;
    private LocalDateTime date;
    private LocalDateTime signupDeadline;
    private int capacity;
    private EventStatus status = EventStatus.DRAFT;

    // Keying signups by user ID prevents duplicates, even across User instances.
    private final Map<String, Signup> attendees = new LinkedHashMap<>();
    // LinkedHashMap preserves arrival order and supports fast removal by user ID.
    // This uses only Java collections, independently of the project's Queue class.
    private final Map<String, Signup> queue = new LinkedHashMap<>();

    public Event(String id, User poster, String name, String description,
                 LocalDateTime date, int capacity, LocalDateTime signupDeadline) {
        this.id = requireText(id, "ID");
        this.poster = Objects.requireNonNull(poster, "Poster is required");
        updateEvent(name, description, date, capacity, signupDeadline);
    }

    public void cancelEvent() {
        EventStatus currentStatus = getStatus();
        if (currentStatus == EventStatus.CANCELLED || currentStatus == EventStatus.DONE) {
            throw new IllegalStateException("A cancelled or completed event cannot be cancelled");
        }
        status = EventStatus.CANCELLED;
    }

    // A null deadline means signups remain open until the event starts.
    public void updateEvent(String name, String description, LocalDateTime date,
                            int capacity, LocalDateTime signupDeadline) {
        requireEditable();
        requireText(name, "Name");
        Objects.requireNonNull(description, "Description is required");
        Objects.requireNonNull(date, "Event date is required");

        LocalDateTime deadline = signupDeadline == null ? date : signupDeadline;
        if (deadline.isAfter(date)) {
            throw new IllegalArgumentException("Signup deadline cannot be after the event starts");
        }
        if (capacity <= 0 || capacity < attendees.size()) {
            throw new IllegalArgumentException("Capacity must be positive and fit all attendees");
        }
        if (status == EventStatus.PUBLISHED && !date.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("A published event must start in the future");
        }

        this.name = name;
        this.description = description;
        this.date = date;
        this.capacity = capacity;
        this.signupDeadline = deadline;
        fillAvailableSpots();
    }

    public void publishEvent() {
        if (status != EventStatus.DRAFT) {
            throw new IllegalStateException("Only draft events can be published");
        }
        if (!date.isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("An event must start in the future to be published");
        }
        status = EventStatus.PUBLISHED;
    }

    // Refresh on access; no background task or thread is created per event.
    private void startEvent() {
        if (status == EventStatus.PUBLISHED && !LocalDateTime.now().isBefore(date)) {
            status = EventStatus.ONGOING;
        }
    }

    public void completeEvent() {
        if (getStatus() != EventStatus.ONGOING) {
            throw new IllegalStateException("Only ongoing events can be completed");
        }
        status = EventStatus.DONE;
    }

    public int getAvailableSpots() {
        return capacity - attendees.size();
    }

    // Full events still accept signups into the waiting queue.
    public boolean canSignup() {
        return getStatus() == EventStatus.PUBLISHED
                && LocalDateTime.now().isBefore(signupDeadline);
    }

    public Signup signUp(User guest, Info info) {
        if (!canSignup()) {
            throw new IllegalStateException("Signups are closed for this event");
        }

        String userId = requireUserId(guest);
        if (attendees.containsKey(userId) || queue.containsKey(userId)) {
            throw new IllegalArgumentException("User is already registered or queued");
        }

        Objects.requireNonNull(info, "Signup info is required");
        // Uses the nested record for now. The team's Signup must offer this constructor.
        // 'this' is the event the guest is signing up for.
        Signup signup = new Signup(guest, this, info);
        if (getAvailableSpots() > 0) {
            attendees.put(userId, signup);
        } else {
            queue.put(userId, signup);
        }
        return signup;
    }

    // Allow withdrawals after the signup deadline, but only before the event starts.
    public void cancelSignup(User guest) {
        if (getStatus() != EventStatus.PUBLISHED) {
            throw new IllegalStateException("Signups can only be cancelled before the event starts");
        }

        String userId = requireUserId(guest);
        if (attendees.remove(userId) != null) {
            fillAvailableSpots();
        } else if (queue.remove(userId) == null) {
            throw new IllegalArgumentException("User is not registered or queued");
        }
    }

    private void fillAvailableSpots() {
        Iterator<Map.Entry<String, Signup>> iterator = queue.entrySet().iterator();
        while (getAvailableSpots() > 0 && iterator.hasNext()) {
            Map.Entry<String, Signup> next = iterator.next();
            attendees.put(next.getKey(), next.getValue());
            iterator.remove();
        }
    }

    private void requireEditable() {
        EventStatus currentStatus = getStatus();
        if (currentStatus != EventStatus.DRAFT && currentStatus != EventStatus.PUBLISHED) {
            throw new IllegalStateException("Only draft or published events can be updated");
        }
    }

    private static String requireUserId(User user) {
        Objects.requireNonNull(user, "User is required");
        // Uses the nested interface for now. The team's User must also expose getId().
        // The same user must keep the same ID across instances and signup/cancellation calls.
        return requireText(user.getId(), "User ID");
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }

    public String getId() {
        return id;
    }

    public User getPoster() {
        return poster;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public LocalDateTime getSignupDeadline() {
        return signupDeadline;
    }

    public int getCapacity() {
        return capacity;
    }

    public EventStatus getStatus() {
        startEvent();
        return status;
    }

    // Immutable snapshots keep callers from bypassing signup rules.
    public List<Signup> getAttendees() {
        return List.copyOf(attendees.values());
    }

    public List<Signup> getQueue() {
        return List.copyOf(queue.values());
    }
}
