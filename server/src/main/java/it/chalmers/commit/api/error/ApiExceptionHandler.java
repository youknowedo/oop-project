package it.chalmers.commit.api.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Renders every exception thrown from a controller as an {@link ApiError}.
 *
 * <p>Spring's own MVC exceptions and {@code ResponseStatusException} keep their HTTP status; the
 * {@link ApiError#code() code} is derived from it. Anything unexpected becomes a 500 with a generic
 * message, and the cause is only logged, never returned.
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<Object> handleUnauthenticated(AuthenticationException ex, WebRequest request) {
        return respond(HttpStatus.UNAUTHORIZED, "Authentication required");
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<Object> handleForbidden(AccessDeniedException ex, WebRequest request) {
        return respond(HttpStatus.FORBIDDEN, "Access denied");
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Object> handleUnexpected(Exception ex, WebRequest request) {
        log.error("Unhandled exception", ex);
        return respond(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    /** Called by the base class for every Spring MVC exception it handles. */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        Object resolved = body;
        if (resolved == null && ex instanceof ErrorResponse errorResponse) {
            resolved = errorResponse.updateAndGetBody(getMessageSource(), LocaleContextHolder.getLocale());
        }
        String message = resolved instanceof ProblemDetail problem && problem.getDetail() != null
                ? problem.getDetail()
                : defaultMessage(statusCode);
        return ResponseEntity.status(statusCode).headers(headers).body(ApiError.of(codeFor(statusCode), message));
    }

    private static ResponseEntity<Object> respond(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ApiError.of(codeFor(status), message));
    }

    private static String codeFor(HttpStatusCode status) {
        HttpStatus resolved = HttpStatus.resolve(status.value());
        return resolved != null ? resolved.name() : "HTTP_" + status.value();
    }

    private static String defaultMessage(HttpStatusCode status) {
        HttpStatus resolved = HttpStatus.resolve(status.value());
        return resolved != null ? resolved.getReasonPhrase() : "Error";
    }
}
