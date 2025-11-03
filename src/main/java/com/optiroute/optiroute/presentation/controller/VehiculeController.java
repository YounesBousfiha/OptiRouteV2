package com.optiroute.optiroute.presentation.controller;


import com.optiroute.optiroute.application.service.VehiculeService;
import com.optiroute.optiroute.presentation.dto.request.VehiculeRequestDTO;
import com.optiroute.optiroute.presentation.dto.response.VehiculeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/vehicle")
public class VehiculeController {

    private VehiculeService vehiculeService;

    public VehiculeController(VehiculeService vehiculeService){
        this.vehiculeService = vehiculeService;
    }

    @PostMapping
    @Operation(summary = "Create a new vehicle", description = "Create a new vehicle with the provided details")
    public ResponseEntity<Long> createVehicule(@RequestBody VehiculeRequestDTO dto) {
        VehiculeResponseDTO vehiculeResponseDTO = this.vehiculeService.createVehicule(dto);
        return ResponseEntity.created(URI.create("/vehicle/" + vehiculeResponseDTO.getId())).body(vehiculeResponseDTO.getId());
    }

    @GetMapping
    @Operation(summary = "Get all vehicles", description = "Retrieve a list of all vehicles")
    public ResponseEntity<List<VehiculeResponseDTO>> getAllVehicules() {
        List<VehiculeResponseDTO> vehiculeResponseDTOS = this.vehiculeService.getAllVehicules();
        return ResponseEntity.ok(vehiculeResponseDTOS);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID", description = "Retrieve a vehicle by its ID")
    public ResponseEntity<VehiculeResponseDTO> getVehiculeById(@PathVariable Long id) {
        VehiculeResponseDTO vehiculeResponseDTO = this.vehiculeService.getVehiculeById(id);
        return ResponseEntity.ok(vehiculeResponseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vehicle", description = "Delete a vehicle by its ID")
    public ResponseEntity<Void> deleteVehicule(@PathVariable Long id) {
        this.vehiculeService.deleteVehicule(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a vehicle", description = "Update the details of an existing vehicle by its ID")
    public ResponseEntity<Void> updateVehicule(@PathVariable Long id, @RequestBody VehiculeRequestDTO dto) {
        this.vehiculeService.updateVehicule(id, dto);
        return ResponseEntity.noContent().build();
    }
}
