package com.amarthya.ticket_api.ticket;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.Test;

class SlaPolicyTest {

    @Test
    void criticalTicketsMustBeHandledWithinFourHours() {
        assertEquals(Duration.ofHours(4), SlaPolicy.targetFor(Priority.CRITICAL));
    }

    @Test
    void highTicketsMustBeHandledWithinEightHours() {
        assertEquals(Duration.ofHours(8), SlaPolicy.targetFor(Priority.HIGH));
    }

    @Test
    void mediumTicketsMustBeHandledWithinOneDay() {
        assertEquals(Duration.ofHours(24), SlaPolicy.targetFor(Priority.MEDIUM));
    }

    @Test
    void lowTicketsMustBeHandledWithinThreeDays() {
        assertEquals(Duration.ofHours(72), SlaPolicy.targetFor(Priority.LOW));
    }
}
