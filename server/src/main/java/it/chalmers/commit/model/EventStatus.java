package it.chalmers.commit.model;

import java.time.LocalDateTime;

public enum EventStatus {
    DRAFT {
        @Override
        EventStatus publish(LocalDateTime now, LocalDateTime startDate) {
            if (!now.isBefore(startDate)) {
                throw new IllegalStateException("An event must start in the future to be published");
            }
            return PUBLISHED;
        }
    },
    PUBLISHED {
        @Override
        EventStatus atTime(LocalDateTime now, LocalDateTime endDate) {
            return now.isBefore(endDate) ? PUBLISHED : FINISHED;
        }
    },
    CANCELLED,
    FINISHED;

    EventStatus publish(LocalDateTime now, LocalDateTime startDate) {
        throw new IllegalStateException("Only draft events can be published");
    }

    EventStatus atTime(LocalDateTime now, LocalDateTime endDate) {
        return this;
    }
}
