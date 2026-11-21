package com.amarthya.ticket_api.ticket;

import jakarta.validation.constraints.NotBlank;

public record CreateTicketRequest(@NotBlank String title, String description, Priority priority) {}
