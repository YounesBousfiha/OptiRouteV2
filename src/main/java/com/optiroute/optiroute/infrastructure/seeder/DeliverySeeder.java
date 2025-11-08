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
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Order(4)
public class DeliverySeeder implements Seeder {

    private final DeliveryRepository deliveryRepository;
    private final CustomerRepository customerRepository;
    private final WareHouseRepository wareHouseRepository;

    @Override
    public void seed() {
        if (deliveryRepository.count() > 0) return;

        List<Customer> customerList = customerRepository.findAll();
        if (customerList.isEmpty()) return; // no customers to assign

        Optional<WareHouse> wareHouse = wareHouseRepository.findById(1L);
        List<Delivery> deliveryList = new ArrayList<>();

        int customerCount = customerList.size();
        for (int i = 0; i < 500; i++) {
            Customer customer = customerList.get(i % customerCount);

            int hour = ThreadLocalRandom.current().nextInt(8, 17);
            LocalDateTime randomTime = LocalDateTime.now().toLocalDate().atTime(hour, 0);

            Delivery delivery = Delivery.builder()
                    .uuid(UUID.randomUUID())
                    .coordinates(new Coordinates(
                            customer.getCoordinates().longitude(),
                            customer.getCoordinates().latitude()))
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
