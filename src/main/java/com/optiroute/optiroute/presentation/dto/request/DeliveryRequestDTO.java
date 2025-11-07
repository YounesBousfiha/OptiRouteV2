package com.optiroute.optiroute.presentation.dto.request;


import com.optiroute.optiroute.domain.entity.Tour;
import com.optiroute.optiroute.domain.enums.DeliveryStatus;
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
public class DeliveryRequestDTO {

    private UUID uuid;

    private Double latitude;

    private Double longitude;

    private Double poids;

    private Double volume;

    private DeliveryStatus deliveryStatus;

    private LocalDateTime plannedDeliveryTime;

    private Tour tour;
}
