package com.optiroute.optiroute.infrastructure.seeder;


import com.optiroute.optiroute.domain.entity.Customer;
import com.optiroute.optiroute.domain.repository.CustomerRepository;
import com.optiroute.optiroute.domain.vo.Address;
import com.optiroute.optiroute.domain.vo.Coordinates;
import com.optiroute.optiroute.domain.vo.PreferredTimeSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(3)
public class CutomerSeeder implements Seeder {

    private final CustomerRepository customerRepository;

    @Override
    public void seed() {
        List<Customer> customerList = new ArrayList<>();

        if(customerRepository.count() > 0) return;
        for (int i = 1; i <= 150; i++) {
            Customer customer = Customer.builder()
                    .name("Customer " + i)
                    .address(new Address(
                            "Street " + i,
                            "City " + i,
                            "State " + i,
                            "ZipCode " + i
                    ))
                    .coordinates(new Coordinates(
                            33.5 + (i * 0.01),
                            -7.6 + (i * 0.01
                    )))
                    .preferredTimeSlot(new PreferredTimeSlot(
                            LocalTime.of(9 + (i % 8), 0),
                            LocalTime.of(17, 0)
                    ))
                    .build();
            customerList.add(customer);
        }

        this.customerRepository.saveAll(customerList);
    }
}
