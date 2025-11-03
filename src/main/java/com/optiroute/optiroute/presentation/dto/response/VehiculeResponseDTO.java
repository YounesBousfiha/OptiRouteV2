package com.optiroute.optiroute.presentation.dto.response;

import com.optiroute.optiroute.domain.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehiculeResponseDTO {
    private Long id;
    private VehicleType vehicleType;
}
