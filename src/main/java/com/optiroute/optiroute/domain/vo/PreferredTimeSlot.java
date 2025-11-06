package com.optiroute.optiroute.domain.vo;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public record PreferredTimeSlot(
        LocalDateTime start,
        LocalDateTime end
) {
}
