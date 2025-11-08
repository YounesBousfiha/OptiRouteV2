package com.optiroute.optiroute.application.service;


import com.optiroute.optiroute.application.helper.UpdateHelper;
import com.optiroute.optiroute.application.mapper.CustomerMapper;
import com.optiroute.optiroute.domain.entity.Customer;
import com.optiroute.optiroute.domain.exception.DatabaseException;
import com.optiroute.optiroute.domain.exception.ResourceNotFoundException;
import com.optiroute.optiroute.domain.repository.CustomerRepository;
import com.optiroute.optiroute.presentation.dto.request.CustomerRequestDTO;
import com.optiroute.optiroute.presentation.dto.request.CustomerUpdateDTO;
import com.optiroute.optiroute.presentation.dto.response.CustomerResponseDTO;
import com.optiroute.optiroute.presentation.dto.response.PagedResponseDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CustomerService {

    private final UpdateHelper updateHelper;
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, UpdateHelper updateHelper) {
        this.customerRepository = customerRepository;
        this.updateHelper = updateHelper;
    }

    public PagedResponseDTO<CustomerResponseDTO> getAllCustomers(int page, int size, String sortBy, String direction) {
        try {
            int adjustedPage = (page <= 0) ? 0 : page - 1;

            Sort sort = direction.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(adjustedPage, size, sort);

            Page<Customer> customerPage = this.customerRepository.findAll(pageable);

            List<CustomerResponseDTO> customers = customerPage.getContent().stream()
                    .map(CustomerMapper::toDTO)
                    .toList();

            return PagedResponseDTO.<CustomerResponseDTO>builder()
                    .data(customers)
                    .currentPage(customerPage.getNumber() + 1)
                    .totalPages(customerPage.getTotalPages())
                    .totalElements(customerPage.getTotalElements())
                    .pageSize(customerPage.getSize())
                    .hasNext(customerPage.hasNext())
                    .hasPrevious(customerPage.hasPrevious())
                    .build();
        } catch (DataAccessException ex) {
            log.error("Database Exception While Fetch All Customer: {}", ex.getMessage());
            throw new DatabaseException("Failed to fetch customers", ex);
        }
    }

    public CustomerResponseDTO createCustomer(@Valid CustomerRequestDTO dto) {
        try {

            log.warn("Creating Customer from DTO: {}", dto);
            Customer customer = CustomerMapper.toEntity(dto);

            log.warn("Creating Customer: {}", customer);
            Customer newCustomer = this.customerRepository.save(customer);

            log.warn("Created Customer: {}", newCustomer);
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

    public CustomerResponseDTO updateCustomer(CustomerUpdateDTO dto , Long id) {

        log.warn("Updating Customer with ID {} using DTO: {}", id, dto);
        try {
            Customer customer = this.customerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("No Such Customer"));

            updateHelper.updateAddress(customer, dto.getCity(), dto.getCountry(), dto.getStreet(), dto.getZipCode());
            updateHelper.updateIfNotNull(dto.getName(), customer::setName);
            updateHelper.updateCoordinates(customer, dto.getLatitude(), dto.getLongitude());
            updateHelper.updatePreferredTimeSlot(customer, dto.getPreferredStartTime(), dto.getPreferredEndTime());

            Customer updatedCustomer = this.customerRepository.save(customer);

            return CustomerMapper.toDTO(updatedCustomer);
        } catch (DatabaseException ex) {
            log.error("Exception while Updating Customer: {}", ex.getMessage());
            throw new DatabaseException("Error while updating Customer Data", ex);
        } catch (ResourceNotFoundException ex) {
            log.warn("No Such Customer with ID: {}", id);
            throw ex;
        }
    }

    public void deleteCustomer(Long id) {
        try {
            Customer customer = this.customerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("No Such Customer"));

            this.customerRepository.delete(customer);

            log.info("Customer with ID {} deleted successfully", id);
        } catch (DatabaseException ex) {
            log.error("Exception while Deleting Customer: {}", ex.getMessage());
            throw new DatabaseException("Error while Deleting Customer", ex);
        } catch (ResourceNotFoundException ex) {
            log.warn("No Such Customer with ID: {}", id);
            throw ex;
        }
    }


    public List<CustomerResponseDTO> getCustomerByName(String word) {
        try {
            return this.customerRepository.findByNameContainingIgnoreCase(word)
                    .stream()
                    .map(CustomerMapper::toDTO)
                    .toList();
        } catch (DatabaseException ex) {
            log.error("Database Exception while Fetch All Customer: {}", ex.getMessage());
            throw new DatabaseException("Failed to fetch customer contain: {}", ex);
        }
    }
}
