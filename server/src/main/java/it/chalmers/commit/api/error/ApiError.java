package it.chalmers.commit.api.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * The body of every error response from the API.
 *
 * @param code stable, machine-readable error code (e.g. {@code NOT_FOUND}); clients branch on this
 * @param message human-readable description, safe to show or log
 * @param fieldErrors per-field problems; empty unless the request failed validation
 */
public record ApiError(
        String code,
        String message,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<FieldError> fieldErrors) {

    public ApiError {
        fieldErrors = fieldErrors == null ? List.of() : List.copyOf(fieldErrors);
    }

    public static ApiError of(String code, String message) {
        return new ApiError(code, message, List.of());
    }

    /**
     * One invalid field.
     *
     * @param field name of the field, using dots for nesting (e.g. {@code address.city})
     * @param message what is wrong with it
     */
    public record FieldError(String field, String message) {}
}
