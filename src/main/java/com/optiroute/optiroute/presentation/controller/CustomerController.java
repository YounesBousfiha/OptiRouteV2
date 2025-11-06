package com.optiroute.optiroute.presentation.controller;


import com.optiroute.optiroute.application.service.CustomerService;
import com.optiroute.optiroute.presentation.dto.request.CustomerRequestDTO;
import com.optiroute.optiroute.presentation.dto.request.CustomerUpdateDTO;
import com.optiroute.optiroute.presentation.dto.response.CustomerResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(CustomerRequestDTO customerRequestDTO) {
        CustomerResponseDTO customerResponseDTO = this.customerService.createCustomer(customerRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customerResponseDTO.getId())
                .toUri();
        return ResponseEntity.created(location).body(customerResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        List<CustomerResponseDTO> customerResponseDTOS = this.customerService.getAllCustomers();
        return ResponseEntity.ok(customerResponseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable("id") Long id) {
        CustomerResponseDTO customerResponseDTO = this.customerService.getOneCustomer(id);
        return ResponseEntity.ok(customerResponseDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable("id") Long id, @RequestBody CustomerUpdateDTO customerUpdateDTO) {
        CustomerResponseDTO customerResponseDTO = this.customerService.updateCustomer(customerUpdateDTO, id);
        return ResponseEntity.ok(customerResponseDTO);
    }
    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Long id) {
        this.customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

}
