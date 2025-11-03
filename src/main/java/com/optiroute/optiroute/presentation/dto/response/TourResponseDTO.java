package com.optiroute.optiroute.presentation.dto.response;


import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.domain.entity.Vehicule;
import com.optiroute.optiroute.domain.entity.WareHouse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourResponseDTO {
    private Long id;

    private Vehicule vehicule;

    private List<DeliveryResponseDTO> deliveryList; // Use the Delivery DTO instread of Delivery Entity

    private WareHouse wareHouse;

    private double totalDistance;

    private LocalDateTime createdAt;
}
