package com.amarthya.ticket_api.ticket;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final TicketRepository repo;

    public TicketService(TicketRepository repo) {
        this.repo = repo;
    }

    public Ticket create(CreateTicketRequest req) {
        Ticket t = new Ticket();
        t.setTitle(req.title());
        t.setDescription(req.description());
        t.setPriority(req.priority() != null ? req.priority() : Priority.MEDIUM);
        LocalDateTime now = LocalDateTime.now();
        t.setCreatedAt(now);
        t.setSlaDueAt(now.plus(SlaPolicy.targetFor(t.getPriority())));
        return repo.save(t);
    }

    public List<Ticket> findAll() {
        return repo.findAll();
    }

    public Ticket findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
    }

    public Ticket updateStatus(Long id, Status status) {
        Ticket t = findById(id);
        t.setStatus(status);
        return repo.save(t);
    }
}
