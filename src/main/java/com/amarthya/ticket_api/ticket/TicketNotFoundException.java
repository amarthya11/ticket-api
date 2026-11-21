package com.amarthya.ticket_api.ticket;

public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException(Long id) {
        super("Ticket " + id + " not found");
    }
}
