package com.optiroute.optiroute.domain.vo;

import jakarta.persistence.Embeddable;

import java.time.LocalTime;

@Embeddable
public record PreferredTimeSlot(
        LocalTime start,
        LocalTime end
) {
}
