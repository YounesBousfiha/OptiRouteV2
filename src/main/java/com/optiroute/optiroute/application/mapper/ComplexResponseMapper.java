package com.optiroute.optiroute.application.mapper;

import com.optiroute.optiroute.domain.entity.DeliveryHistory;
import com.optiroute.optiroute.presentation.dto.response.ComplexResponseDTO;
import com.optiroute.optiroute.presentation.dto.response.DeliveryResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ComplexResponseMapper {

    private ComplexResponseMapper() {
        /* private constructur */
    }

    public static ComplexResponseDTO toDTO(DeliveryHistory entity) {
        if(null == entity) return null;
        return ComplexResponseDTO.builder()
                .deliveryHistoryId(entity.getId())
                .actualDeliveryTime(entity.getActualDeliveryTime())
                .plannedDeliveryTime(entity.getPlannedDeliveryTime())
                .delayInMinutes(entity.getDelayInMinutes())
                .dayOfWeek(String.valueOf(entity.getDayOfWeek()))
                .deliveryResponseDTO(
                        DeliveryResponseDTO.builder()
                                .id(entity.getDelivery().getId())
                                .uuid(entity.getDelivery().getUuid())
                                .longitude(entity.getDelivery().getCoordinates().longitude())
                                .latitude(entity.getDelivery().getCoordinates().latitude())
                                .poids(entity.getDelivery().getPoids())
                                .volume(entity.getDelivery().getVolume())
                                .tourId(entity.getDelivery().getTour().getId())
                                .deliveryStatus(String.valueOf(entity.getDelivery().getDeliveryStatus()))
                                .build()
                )
                .build();
    }
}
