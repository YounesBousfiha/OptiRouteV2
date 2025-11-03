package com.optiroute.optiroute.domain.vo;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;


@Embeddable
public record Coordinates(
        @NotNull Double longitude,
        @NotNull Double latitude
) {
}