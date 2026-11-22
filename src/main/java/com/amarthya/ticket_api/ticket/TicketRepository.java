package com.amarthya.ticket_api.ticket;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findBySlaDueAtBeforeAndStatusNotIn(LocalDateTime time, Collection<Status> statuses);
}
