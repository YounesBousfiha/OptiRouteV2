package com.optiroute.optiroute.presentation.dto.response;

import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.domain.entity.Tour;
import com.optiroute.optiroute.domain.vo.Address;
import com.optiroute.optiroute.domain.vo.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WareHouseResponseDTO {
    private Long id;
    private LocalTime openHour;
    private LocalTime closeHour;
    private Address address;
    private Coordinates coordinates;
    private List<Tour> tourIds;
    private List<Delivery> availableDeliveries;
    private LocalDateTime createdAt;

}
