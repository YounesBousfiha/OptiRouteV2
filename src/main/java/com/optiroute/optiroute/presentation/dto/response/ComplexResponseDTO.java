package com.optiroute.optiroute.presentation.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ComplexResponseDTO {

    private Long deliveryHistoryId;
    private LocalDateTime plannedDeliveryTime;
    private LocalDateTime actualDeliveryTime;
    private long delayInMinutes;
    private String dayOfWeek;

    private DeliveryResponseDTO deliveryResponseDTO;
}
