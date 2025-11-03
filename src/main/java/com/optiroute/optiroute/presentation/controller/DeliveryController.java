package com.optiroute.optiroute.presentation.controller;


import com.optiroute.optiroute.application.service.DeliveryService;
import com.optiroute.optiroute.presentation.dto.request.DeliveryRequestDTO;
import com.optiroute.optiroute.presentation.dto.response.DeliveryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    @Operation(summary = "Get all deliveries", description = "Retrieve a list of all deliveries")
    public ResponseEntity<List<DeliveryResponseDTO>> getAllDeliveries() {
        List<DeliveryResponseDTO> deliveryResponseDTOS = this.deliveryService.getAllDeliveries();
        return ResponseEntity.ok(deliveryResponseDTOS);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get delivery by ID", description = "Retrieve a delivery by its ID")
    public ResponseEntity<DeliveryResponseDTO> getDeliveryById(@PathVariable Long id) {
        DeliveryResponseDTO deliveryResponseDTO = this.deliveryService.getDeliveryById(id);
        return ResponseEntity.ok(deliveryResponseDTO);
    }

    @PostMapping
    @Operation(summary = "Create a new delivery", description = "Create a new delivery with the provided details")
    public ResponseEntity<Long> createDelivery(@RequestBody DeliveryRequestDTO dto) {
        DeliveryResponseDTO deliveryResponseDTO = this.deliveryService.createDelivery(dto);
        return ResponseEntity.created(URI.create("/delivery/" + deliveryResponseDTO.getId())).body(deliveryResponseDTO.getId());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a delivery", description = "Update the details of an existing delivery by its ID")
    public ResponseEntity<Void> updateDelivery(@PathVariable Long id, @RequestBody DeliveryRequestDTO dto) {
        this.deliveryService.updateDelivery(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a delivery", description = "Delete a delivery by its ID")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        this.deliveryService.deleteDelivery(id);
        return ResponseEntity.noContent().build();
    }

}
