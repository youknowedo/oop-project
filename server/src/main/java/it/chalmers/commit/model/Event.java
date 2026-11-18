package it.chalmers.commit.model;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** An event with lifecycle rules, attendee signups and a FIFO waiting queue. */
public final class Event {
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
        this.status = EventStatus.CANCELLED;
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
        if (this.status == EventStatus.PUBLISHED && !date.isAfter(LocalDateTime.now())) {
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
        if (this.status != EventStatus.DRAFT) {
            throw new IllegalStateException("Only draft events can be published");
        }
        if (!date.isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("An event must start in the future to be published");
        }
        this.status = EventStatus.PUBLISHED;
    }

    // Refresh on access; no background task or thread is created per event.
    private void startEvent() {
        if (this.status == EventStatus.PUBLISHED && !LocalDateTime.now().isBefore(date)) {
            this.status = EventStatus.ONGOING;
        }
    }

    public void completeEvent() {
        if (getStatus() != EventStatus.ONGOING) {
            throw new IllegalStateException("Only ongoing events can be completed");
        }
        this.status = EventStatus.DONE;
    }

    public int getAvailableSpots() {
        return capacity - attendees.size();
    }

    // Full events still accept signups into the waiting queue.
    public boolean canSignup() {
        return getStatus() == EventStatus.PUBLISHED
                && LocalDateTime.now().isBefore(signupDeadline);
    }

    // TODO(team): When merging the Signup and Queue implementations, check which signup
    // operations they already handle. Delegate through their agreed APIs and remove duplicated
    // validation, duplicate detection, Signup creation or queue insertion below where applicable.
    // Keep event-specific rules enforced: signups must be open, each user can register only once,
    // and confirmed attendees must not exceed capacity. If another component handles the entire
    // signup flow, replace this body with delegation rather than maintaining two implementations.
    public Signup signUp(User guest, Info info) {
        if (!canSignup()) {
            throw new IllegalStateException("Signups are closed for this event");
        }

        String userId = requireUserId(guest);
        if (attendees.containsKey(userId) || queue.containsKey(userId)) {
            throw new IllegalArgumentException("User is already registered or queued");
        }

        Objects.requireNonNull(info, "Signup info is required");
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
    // TODO(team): If the merged Signup/Queue code handles cancellation, delegate to its API and
    // remove duplicated attendee/queue removal and promotion logic below. Ensure cancellation
    // still respects the event's status, removes the registration from the correct collection,
    // and promotes the next queued signup exactly once when an attendee's spot becomes available.
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

    // TODO(team): When merging the self-implemented Queue class, replace the map iterator
    // logic below with Queue's API for getting/removing the next signup. Remove the duplicated
    // queue-ordering and removal logic here; let Queue determine who is next. Event should still
    // fill available spots and add promoted signups to attendees. If Queue already handles the
    // entire promotion process, remove this method and replace its calls in updateEvent() and
    // cancelSignup() with the agreed Queue operation. Also adapt the queue field, signUp(),
    // duplicate detection, cancellation and getQueue() to that API, removing obsolete map code
    // and the Iterator import once it is no longer used.
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
        return this.status;
    }

    // Immutable snapshots keep callers from bypassing signup rules.
    public List<Signup> getAttendees() {
        return List.copyOf(attendees.values());
    }

    public List<Signup> getQueue() {
        return List.copyOf(queue.values());
    }
}
