package com.optiroute.optiroute.presentation.dto.request;

import com.optiroute.optiroute.domain.vo.Address;
import com.optiroute.optiroute.domain.vo.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WareHouseRequestDTO {
    private LocalTime openHour;
    private LocalTime closeHour;
    private Address address;
    private Coordinates coordinates;
}
