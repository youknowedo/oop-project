package it.chalmers.commit.model;

public class User {
    private String id;
    private String name;
    private Role role;
    private String[] allergies = new String[0];

    public User(
        String id,
        String name,
        Role role,
        String[] allergies,
    ) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.allergies = allergies;
    }

}

/* Publisher {
    String id;
    String name;
    String role = Publisher;
    Event[] events;    
} */

/* Guest {
    String id;
    String name,
    // more personal info? 
    String role = Guest;
    registreredEvents[]?

} */