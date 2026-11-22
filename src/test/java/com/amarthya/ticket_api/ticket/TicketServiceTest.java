package com.amarthya.ticket_api.ticket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository repo;

    @InjectMocks
    private TicketService service;

    @Test
    void createDefaultsToMediumPriorityWith24HourSla() {
        when(repo.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        Ticket created = service.create(new CreateTicketRequest("Wifi down", "No signal", null));

        assertEquals(Priority.MEDIUM, created.getPriority());
        assertEquals(Status.OPEN, created.getStatus());
        assertEquals(Duration.ofHours(24),
                Duration.between(created.getCreatedAt(), created.getSlaDueAt()));
    }

    @Test
    void createHighPriorityGetsEightHourSla() {
        when(repo.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        Ticket created = service.create(new CreateTicketRequest("Router fault", "Red light", Priority.HIGH));

        assertEquals(Priority.HIGH, created.getPriority());
        assertEquals(Duration.ofHours(8),
                Duration.between(created.getCreatedAt(), created.getSlaDueAt()));
    }

    @Test
    void findByIdThrowsWhenTicketIsMissing() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void updateStatusChangesAndSavesTicket() {
        Ticket ticket = new Ticket();
        when(repo.findById(1L)).thenReturn(Optional.of(ticket));
        when(repo.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        Ticket updated = service.updateStatus(1L, Status.IN_PROGRESS);

        assertEquals(Status.IN_PROGRESS, updated.getStatus());
    }

    @Test
    void escalateMovesOpenTicketToEscalated() {
        Ticket ticket = new Ticket();
        when(repo.findById(1L)).thenReturn(Optional.of(ticket));
        when(repo.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        Ticket escalated = service.escalate(1L);

        assertEquals(Status.ESCALATED, escalated.getStatus());
    }

    @Test
    void escalateRejectsResolvedTicket() {
        Ticket ticket = new Ticket();
        ticket.setStatus(Status.RESOLVED);
        when(repo.findById(1L)).thenReturn(Optional.of(ticket));

        assertThrows(IllegalStateException.class, () -> service.escalate(1L));
        verify(repo, never()).save(any(Ticket.class));
    }

    @Test
    void findBreachedExcludesResolvedAndClosedTickets() {
        service.findBreached();

        verify(repo).findBySlaDueAtBeforeAndStatusNotIn(
                any(LocalDateTime.class), eq(List.of(Status.RESOLVED, Status.CLOSED)));
    }
}