package com.optiroute.optiroute.domain.vo;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;



@Embeddable
public record Address(
        @NotNull String city,
        @NotNull String country,
        @NotNull String street,
        @NotNull String postalCode
        ) {
}
