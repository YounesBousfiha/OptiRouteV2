package com.optiroute.optiroute.application.mapper;

import com.optiroute.optiroute.domain.entity.Customer;
import com.optiroute.optiroute.domain.vo.Address;
import com.optiroute.optiroute.domain.vo.Coordinates;
import com.optiroute.optiroute.domain.vo.PreferredTimeSlot;
import com.optiroute.optiroute.presentation.dto.request.CustomerRequestDTO;
import com.optiroute.optiroute.presentation.dto.request.CustomerUpdateDTO;
import com.optiroute.optiroute.presentation.dto.response.CustomerResponseDTO;

import java.time.LocalDateTime;

public class CustomerMapper {

    private CustomerMapper() {
        /* for SonarQube */
    }
    public static CustomerResponseDTO toDTO(Customer customer) {
        if(null == customer) return null;

        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .address(customer.getAddress())
                .coordinates(customer.getCoordinates())
                .preferredTimeSlot(customer.getPreferredTimeSlot())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdateAt())
                .build();
    }

    public static Customer toEntity(CustomerRequestDTO dto) {
        if(null == dto) return null;

        return Customer.builder()
                .name(dto.getName())
                .address(new Address(dto.getCity(), dto.getCountry(), dto.getStreet(), dto.getZipCode()))
                .coordinates(new Coordinates(dto.getLongitude(), dto.getLatitude()))
                .preferredTimeSlot(new PreferredTimeSlot(
                        LocalDateTime.parse(dto.getPreferredStartTime()),
                        LocalDateTime.parse(dto.getPreferredEndTime())))
                .build();
    }

    public static Customer toUpdateEntity(CustomerUpdateDTO dto) {
        if ( null == dto ) return null;

        return Customer.builder()
                .name(dto.getName())
                .address(new Address(dto.getCity(), dto.getCountry(), dto.getStreet(), dto.getZipCode()))
                .coordinates(new Coordinates(dto.getLongitude(), dto.getLatitude()))
                .preferredTimeSlot(new PreferredTimeSlot(
                        dto.getPreferredStartTime() != null ? LocalDateTime.parse(dto.getPreferredStartTime()) : null,
                        dto.getPreferredEndTime() != null ? LocalDateTime.parse(dto.getPreferredEndTime()) : null))
                .build();
    }
}
