package com.optiroute.optiroute.presentation.dto;


import com.optiroute.optiroute.domain.entity.Tour;
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
public class WareHouseDTO {
    private Long id;
    private LocalTime openHour;
    private LocalTime closeHour;
    private String address;
    private LocalDateTime created_at;
    private List<Tour> tourIds;
}
