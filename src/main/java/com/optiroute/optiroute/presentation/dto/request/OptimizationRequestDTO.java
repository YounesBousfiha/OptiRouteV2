package com.optiroute.optiroute.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptimizationRequestDTO {
    private Long warehouseId;
    private Long vehiculeId;
}
