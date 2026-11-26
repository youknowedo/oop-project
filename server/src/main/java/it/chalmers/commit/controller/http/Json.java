package it.chalmers.commit.controller.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import java.time.Instant;

/** Turns view objects into JSON and request bodies back into objects. */
public final class Json {

  private static final Gson GSON =
      new GsonBuilder()
          .registerTypeAdapter(
              Instant.class,
              (JsonSerializer<Instant>)
                  (instant, type, context) -> context.serialize(instant.toString()))
          .registerTypeAdapter(
              Instant.class,
              (JsonDeserializer<Instant>)
                  (json, type, context) -> Instant.parse(json.getAsString()))
          .create();

  private Json() {}

  public static String toJson(Object value) {
    return GSON.toJson(value);
  }

  /** Parses the JSON, or throws {@link IllegalArgumentException} if it is not valid. */
  public static <T> T fromJson(String json, Class<T> type) {
    try {
      T value = GSON.fromJson(json, type);
      if (value == null) {
        throw new IllegalArgumentException("Request body is empty");
      }
      return value;
    } catch (JsonSyntaxException e) {
      throw new IllegalArgumentException("Request body is not valid JSON", e);
    }
  }
}
