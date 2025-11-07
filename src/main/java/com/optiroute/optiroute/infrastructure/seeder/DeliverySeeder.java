package com.optiroute.optiroute.infrastructure.seeder;

import com.optiroute.optiroute.domain.entity.Customer;
import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.domain.entity.WareHouse;
import com.optiroute.optiroute.domain.enums.DeliveryStatus;
import com.optiroute.optiroute.domain.repository.CustomerRepository;
import com.optiroute.optiroute.domain.repository.DeliveryRepository;
import com.optiroute.optiroute.domain.repository.WareHouseRepository;
import com.optiroute.optiroute.domain.vo.Coordinates;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Order(4)
public class DeliverySeeder implements Seeder {

    private final DeliveryRepository deliveryRepository;
    private final CustomerRepository customerRepository;
    private final WareHouseRepository wareHouseRepository;

    @Override
    public void seed() {
        if(deliveryRepository.count() > 0) return;
        List<Delivery> deliveryList = new ArrayList<>();
        List<Customer> customerList = customerRepository.findAll();
        Optional<WareHouse> wareHouse = wareHouseRepository.findById(1L);

        for (int i = 1; i < customerRepository.count() ; i++) {
            Customer customer = customerList.get(i);
            LocalDateTime randomTime = LocalDateTime.now().toLocalDate().atTime((int)(Math.random() * 9) + 8, 0);
            Delivery delivery = Delivery.builder()
                    .uuid(UUID.randomUUID())
                    .coordinates(
                            new Coordinates(
                                    customer.getCoordinates().longitude(),
                                    customer.getCoordinates().latitude())
                    )
                    .deliveryStatus(DeliveryStatus.PENDING)
                    .poids(10 + Math.random() * 50)
                    .volume(0.1 + Math.random() * 2)
                    .warehouse(wareHouse.orElse(null))
                    .plannedDeliveryTime(randomTime)
                    .customer(customer)
                    .isOptimized(false)
                    .build();
            deliveryList.add(delivery);
        }
        this.deliveryRepository.saveAll(deliveryList);
    }
}
