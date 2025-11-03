package com.optiroute.optiroute.application.mapper;

import com.optiroute.optiroute.domain.entity.Vehicule;
import com.optiroute.optiroute.presentation.dto.request.VehiculeRequestDTO;
import com.optiroute.optiroute.presentation.dto.response.VehiculeResponseDTO;

public class VehiculeMapper {


    public static Vehicule toEntity(VehiculeRequestDTO dto) {
        if(null == dto ) return null;

        return Vehicule.builder()
                .vehicleType(dto.getVehicleType())
                .build();
    }


    public static VehiculeResponseDTO toResponseDTO(Vehicule vehicule) {
        if (null == vehicule) return null;

        return VehiculeResponseDTO.builder()
                .id(vehicule.getId())
                .vehicleType(vehicule.getVehicleType())
                .build();
    }
}
