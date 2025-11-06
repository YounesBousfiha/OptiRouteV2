package com.optiroute.optiroute.presentation.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateDTO {
    private String name;

    private String street;

    private String city;

    private String country;

    private String state;

    private String zipCode;

    private Double latitude;

    private Double longitude;

    @Pattern(regexp = "^([01]?\\d|2[0-3]):[0-5]\\d$", message = "Start time must be in HH:mm format")
    private String preferredStartTime;

    @Pattern(regexp = "^([01]?\\d|2[0-3]):[0-5]\\d$", message = "Start time must be in HH:mm format")
    private String preferredEndTime;
}
