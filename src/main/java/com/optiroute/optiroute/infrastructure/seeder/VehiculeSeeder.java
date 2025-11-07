package com.optiroute.optiroute.infrastructure.seeder;

import com.optiroute.optiroute.domain.entity.Vehicule;
import com.optiroute.optiroute.domain.enums.VehicleType;
import com.optiroute.optiroute.domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(2)
public class VehiculeSeeder  implements Seeder {

    private final VehicleRepository vehicleRepository;

    @Override
    public void seed() {
        if(vehicleRepository.count() > 0) return;
        List<Vehicule> vehiculeList = List.of(
                Vehicule.builder()
                        .vehicleType(VehicleType.TRUCK)
                        .build(),
                Vehicule.builder()
                        .vehicleType(VehicleType.VAN)
                        .build(),
                Vehicule.builder()
                        .vehicleType(VehicleType.BIKE)
                        .build()
        );

        vehicleRepository.saveAll(vehiculeList);
    }
}
