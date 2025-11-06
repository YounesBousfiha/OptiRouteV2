package com.optiroute.optiroute.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CustomerRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Country is required")
    private String country;

    private String state;

    @NotBlank(message = "Zip code is required")
    private String zipCode;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    @Pattern(regexp = "^([01]?\\d|2[0-3]):[0-5]\\d$", message = "Start time must be in HH:mm format")
    private String preferredStartTime;

    @Pattern(regexp = "^([01]?\\d|2[0-3]):[0-5]\\d$", message = "Start time must be in HH:mm format")
    private String preferredEndTime;
}
