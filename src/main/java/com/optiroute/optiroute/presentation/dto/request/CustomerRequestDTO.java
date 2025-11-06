package com.optiroute.optiroute.presentation.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDTO {
    private String name;
    private String street;
    private String city;
    private String country;
    private String state;
    private String zipCode;
    private Double latitude;
    private Double longitude;
    private String preferredStartTime;
    private String preferredEndTime;
}
