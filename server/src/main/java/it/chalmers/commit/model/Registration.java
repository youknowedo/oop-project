package it.chalmers.commit.model;

/** One user's place in a {@link Registrations} list, either waiting or admitted. */
public class Registration {

  private final User user;
  private RegistrationStatus status = RegistrationStatus.QUEUED;

  Registration(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  public RegistrationStatus getStatus() {
    return status;
  }

  /** Gives the user a spot. Only {@link Registrations} calls this. */
  void admit() {
    status = RegistrationStatus.ATTENDING;
  }
}
