package com.amarthya.ticket_api.ticket;

import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(@NotNull Status status) {}
