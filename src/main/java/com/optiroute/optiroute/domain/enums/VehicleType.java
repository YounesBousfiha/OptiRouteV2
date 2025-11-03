package com.optiroute.optiroute.domain.enums;

import lombok.Getter;

@Getter
public enum VehicleType {
    VAN(1200, 10),
    TRUCK(8000, 40),
    BIKE(30, 0.1);

    private final double maxWeightKg;
    private final double maxVolumeM3;


    VehicleType(double maxWeightKg, double maxVolumeM3) {
        this.maxVolumeM3 = maxVolumeM3;
        this.maxWeightKg = maxWeightKg;
    }
}
