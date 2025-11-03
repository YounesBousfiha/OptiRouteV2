package com.optiroute.optiroute.application.mapper;

import com.optiroute.optiroute.domain.entity.WareHouse;
import com.optiroute.optiroute.domain.vo.Coordinates;
import com.optiroute.optiroute.presentation.dto.request.WareHouseRequestDTO;
import com.optiroute.optiroute.presentation.dto.response.WareHouseResponseDTO;

public class WareHouseMapper {

    private WareHouseMapper() {
        /* Declare Private Construct */
    }

    public static WareHouse toEntity(WareHouseRequestDTO dto) {
        if(null == dto) return null;

        return WareHouse.builder()
                .openHour(dto.getOpenHour())
                .closeHour(dto.getCloseHour())
                .address(dto.getAddress())
                .coordinates(
                        new Coordinates(
                                dto.getCoordinates().longitude(),
                                dto.getCoordinates().latitude())
                )
                .build();
    }

    public static WareHouseResponseDTO toResponseDTO(WareHouse wareHouse) {
        if(null == wareHouse) return null;
        return WareHouseResponseDTO.builder()
                .id(wareHouse.getId())
                .openHour(wareHouse.getOpenHour())
                .closeHour(wareHouse.getCloseHour())
                .address(wareHouse.getAddress())
                .coordinates(
                        new Coordinates(
                                wareHouse.getCoordinates().longitude(),
                                wareHouse.getCoordinates().latitude()
                        ))
                .tourIds(wareHouse.getTours())
                .availableDeliveries(wareHouse.getAvailableDeliveries())
                .createdAt(wareHouse.getCreatedAt())
                .build();
    }
}
