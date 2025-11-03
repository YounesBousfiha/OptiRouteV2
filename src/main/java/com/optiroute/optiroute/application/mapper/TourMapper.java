package com.optiroute.optiroute.application.mapper;

import com.optiroute.optiroute.domain.entity.Tour;
import com.optiroute.optiroute.presentation.dto.request.TourRequestDTO;
import com.optiroute.optiroute.presentation.dto.response.TourResponseDTO;

import static java.util.stream.Collectors.toList;

public class TourMapper {

    private TourMapper() {
        /* Declare private Construct */
    }

    public static Tour toEntity(TourRequestDTO dto) {
        if(null == dto) return null;

        return Tour.builder()
                .vehicule(dto.getVehicule())
                .wareHouse(dto.getWareHouse())
                .build();
    }


    public static TourResponseDTO tourResponseDTO(Tour tour) {
        if(null == tour) return null;

        return TourResponseDTO.builder()
                .id(tour.getId())
                .vehicule(tour.getVehicule())
                .deliveryList(
                        tour.getDeliveryList().stream()
                                .map(DeliveryMapper::toResponseDTO)
                                .toList())
                .wareHouse(tour.getWareHouse())
                .totalDistance(tour.getTotalDistance())
                .createdAt(tour.getCreatedAt())
                .build();
    }
}
