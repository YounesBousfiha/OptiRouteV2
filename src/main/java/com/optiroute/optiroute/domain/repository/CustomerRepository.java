package com.optiroute.optiroute.domain.repository;

import com.optiroute.optiroute.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
