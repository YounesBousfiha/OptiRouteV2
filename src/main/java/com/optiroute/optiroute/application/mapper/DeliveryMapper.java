package com.optiroute.optiroute.application.mapper;

import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.domain.vo.Coordinates;
import com.optiroute.optiroute.presentation.dto.request.DeliveryRequestDTO;
import com.optiroute.optiroute.presentation.dto.response.DeliveryResponseDTO;

public class DeliveryMapper {

    private DeliveryMapper() {
        // Private constructor to prevent instantiation
    }

    public static Delivery toEntity(DeliveryRequestDTO dto) {
        if (null == dto) return null;

        return Delivery.builder()
                .tour(dto.getTour())
                .uuid(dto.getUuid())
                .coordinates(new Coordinates(dto.getLongitude(), dto.getLatitude()))
                .poids(dto.getPoids())
                .volume(dto.getVolume())
                .deliveryStatus(dto.getDeliveryStatus())
                .plannedDeliveryTime(dto.getPlannedDeliveryTime())
                .build();
    }


    public static DeliveryResponseDTO toResponseDTO(Delivery delivery) {
        if (null == delivery) return null;

        return DeliveryResponseDTO.builder()
                .id(delivery.getId())
                .uuid(delivery.getUuid())
                .latitude(delivery.getCoordinates().latitude())
                .longitude(delivery.getCoordinates().longitude())
                .poids(delivery.getPoids())
                .volume(delivery.getVolume())
                .deliveryStatus(String.valueOf(delivery.getDeliveryStatus()))
                .tourId(delivery.getTour() != null ? delivery.getId() : null)
                .plannedDeliveryTime(delivery.getPlannedDeliveryTime())
                .actualDeliveryTime(delivery.getActualDeliveryTime())
                .createdAt(delivery.getCreated_at())
                .build();
    }
}
