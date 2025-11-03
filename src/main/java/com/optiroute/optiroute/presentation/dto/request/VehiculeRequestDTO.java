package com.optiroute.optiroute.presentation.dto.request;


import com.optiroute.optiroute.domain.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehiculeRequestDTO {
    private VehicleType vehicleType;
}
