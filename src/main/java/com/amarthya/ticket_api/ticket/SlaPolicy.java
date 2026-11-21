package com.amarthya.ticket_api.ticket;

import java.time.Duration;

public final class SlaPolicy {

    private SlaPolicy() {}

    public static Duration targetFor(Priority priority) {
        return switch (priority) {
            case CRITICAL -> Duration.ofHours(4);
            case HIGH -> Duration.ofHours(8);
            case MEDIUM -> Duration.ofHours(24);
            case LOW -> Duration.ofHours(72);
        };
    }
}
