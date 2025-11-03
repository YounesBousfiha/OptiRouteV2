package com.optiroute.optiroute.presentation.dto.request;


import com.optiroute.optiroute.domain.entity.Vehicule;
import com.optiroute.optiroute.domain.entity.WareHouse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourRequestDTO {
    private Long vehiculeId;

    private Long wareHouseId;

    private Vehicule vehicule;

    private WareHouse wareHouse;
}
