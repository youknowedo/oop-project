package it.chalmers.commit.auth;

/**
 * The caller of a request, as identified by Gamma. Deliberately lightweight: it is built from the
 * login and held in the session, with no database behind it.
 *
 * @param gammaId Gamma's stable id for the user
 * @param name the user's display name
 */
public record AuthenticatedUser(String gammaId, String name) {}
