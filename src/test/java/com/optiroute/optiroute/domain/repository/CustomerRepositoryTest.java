package com.optiroute.optiroute.domain.repository;

import com.optiroute.optiroute.domain.entity.Customer;
import com.optiroute.optiroute.domain.vo.Address;
import com.optiroute.optiroute.domain.vo.Coordinates;
import com.optiroute.optiroute.domain.vo.PreferredTimeSlot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;


    @Test
    @DisplayName("Save and retrieve a customer")
    void testSaveAndFindCustomer() {
        Customer customer = Customer.builder()
                .name("Younes")
                .address(new Address(
                        "Casablanca",
                        "Morroco",
                        "123 XYZ Street",
                        "24000"
                ))
                .coordinates(new Coordinates(
                        34.987,
                        -7.123
                ))
                .preferredTimeSlot(new PreferredTimeSlot(
                        LocalTime.of(9, 0),
                        LocalTime.of(18, 0)
                ))
                .build();


        Customer saved = customerRepository.save(customer);

        Optional<Customer> found = customerRepository.findById(saved.getId());


        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Younes");
    }

    @Test
    @DisplayName("save and Delete")
    void testSaveAndDeleteCustomer() {
        Customer customer = Customer.builder()
                .name("Younes")
                .address(new Address(
                        "Casablanca",
                        "Morroco",
                        "123 XYZ Street",
                        "24000"
                ))
                .coordinates(new Coordinates(
                        34.987,
                        -7.123
                ))
                .preferredTimeSlot(new PreferredTimeSlot(
                        LocalTime.of(9, 0),
                        LocalTime.of(18, 0)
                ))
                .build();


       Customer saved =  this.customerRepository.save(customer);
       Optional<Customer> found = customerRepository.findById(saved.getId());

       this.customerRepository.deleteById(found.get().getId());

       boolean isExist = customerRepository.existsById(found.get().getId());

       assertThat(isExist).isFalse();
    }
}