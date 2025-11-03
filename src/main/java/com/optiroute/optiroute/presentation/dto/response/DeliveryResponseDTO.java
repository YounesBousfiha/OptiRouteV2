package com.optiroute.optiroute.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryResponseDTO {

    private Long id;

    private UUID uuid;

    private Double latitude;

    private Double longitude;

    private String deliveryStatus;

    private Double poids;

    private Double volume;

    private Long tourId;

    private LocalDateTime created_at;

}
