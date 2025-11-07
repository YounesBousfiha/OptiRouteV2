package com.optiroute.optiroute.infrastructure.seeder;

import com.optiroute.optiroute.domain.entity.WareHouse;
import com.optiroute.optiroute.domain.repository.WareHouseRepository;
import com.optiroute.optiroute.domain.vo.Address;
import com.optiroute.optiroute.domain.vo.Coordinates;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
@Order(1)
public class WareHouseSeeder implements Seeder {

    private final WareHouseRepository wareHouseRepository;


    @Override
    public void seed() {
        if(wareHouseRepository.count() > 0) return;

        wareHouseRepository.save(
                WareHouse.builder()
                        .coordinates(new Coordinates(-7.6097716,33.5810628))
                        .openHour(LocalTime.parse("08:00"))
                        .closeHour(LocalTime.parse("18:00"))
                        .address(
                                new Address(
                                        "Casablanca",
                                        "Morocco",
                                        "Al Fida-Mers Sultan",
                                        "10000"
                                )
                        )
                        .build()
        );
    }
}
