## Testing

Never write regression tests unless I explicitly request it.

## Architecture rules

See `ARCHITECTURE.md`, section "Persistence and schema".

- `model` imports nothing from Spring or JPA. No `@Entity`, `@Service` or similar annotations in it.
- `persistence` has its own JPA entities and mappers to and from the model classes. Do not annotate model classes.
- Any schema change comes with a Flyway migration in the same PR. Never rely on `ddl-auto=update`.
