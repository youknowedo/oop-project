package it.chalmers.commit.controller;

/** Thrown by a handler to answer with an error status instead of a normal response. */
public class HttpException extends RuntimeException {

  private final int status;

  public HttpException(int status, String message) {
    super(message);
    this.status = status;
  }

  public int status() {
    return status;
  }

  public static HttpException notFound(String message) {
    return new HttpException(404, message);
  }

  public static HttpException badRequest(String message) {
    return new HttpException(400, message);
  }
}
