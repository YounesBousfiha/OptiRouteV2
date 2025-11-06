package com.optiroute.optiroute.application.service;


import com.optiroute.optiroute.application.mapper.CustomerMapper;
import com.optiroute.optiroute.domain.entity.Customer;
import com.optiroute.optiroute.domain.exception.DatabaseException;
import com.optiroute.optiroute.domain.exception.ResourceNotFoundException;
import com.optiroute.optiroute.domain.repository.CustomerRepository;
import com.optiroute.optiroute.presentation.dto.request.CustomerRequestDTO;
import com.optiroute.optiroute.presentation.dto.response.CustomerResponseDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerService {

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        try {
            return customerRepository.findAll().stream()
                    .map(CustomerMapper::toDTO)
                    .toList();
        } catch (DataAccessException ex) {
            log.error("Database Exception While Fetch All Customer: {}", ex.getMessage());
            throw new DatabaseException("Failed to fetch customers", ex);
        }
    }

    public CustomerResponseDTO createCustomer(@Valid CustomerRequestDTO dto) {
        try {
            Customer customer = CustomerMapper.toEntity(dto);

            Customer newCustomer = this.customerRepository.save(customer);

            return CustomerMapper.toDTO(newCustomer);
        } catch (Exception ex) {
            log.error("Exception while create Customer: {}", ex.getMessage());
            throw new DatabaseException("Error while Creating Customer", ex);
        }
    }

    public CustomerResponseDTO getOneCustomer(Long id) {
        try {
            Customer customer = this.customerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("No Such Customer"));

            return CustomerMapper.toDTO(customer);
        } catch (DatabaseException ex) {
            log.error("Exception while Fetching one Customer: {}", ex.getMessage());
            throw new DatabaseException("Error while Fetching one Customer", ex);
        } catch (ResourceNotFoundException ex) {
            log.warn("No Such Customer with ID: {}", id);
            throw ex;
        }
    }

    public CustomerResponseDTO updateCustomer(Long id) {
        return null;
    }

    public boolean deleteCustomer(Long id) {
        return true;
    }


}
