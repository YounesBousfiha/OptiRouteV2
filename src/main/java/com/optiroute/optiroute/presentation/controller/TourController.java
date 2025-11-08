package com.optiroute.optiroute.presentation.controller;


import com.optiroute.optiroute.application.service.TourService;
import com.optiroute.optiroute.presentation.dto.request.OptimizationRequestDTO;
import com.optiroute.optiroute.presentation.dto.request.TourRequestDTO;
import com.optiroute.optiroute.presentation.dto.response.TourResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tours")
public class TourController {

    private final TourService tourService;
    private final Logger logger = LoggerFactory.getLogger(TourController.class);

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }


    @GetMapping
    @Operation(summary = "Get all tours", description = "Retrieve a list of all tours")
    public ResponseEntity<List<TourResponseDTO>> getAllTours() {
        List<TourResponseDTO> tourResponseDTOS = this.tourService.getAllTours();
        return ResponseEntity.ok(tourResponseDTOS);
    }

    @PostMapping
    @Operation(summary = "Create a new tour", description = "Create a new tour with the provided details")
    public ResponseEntity<Long> createTour(@RequestBody TourRequestDTO dto) {
        TourResponseDTO tourResponseDTO = this.tourService.createTour(dto);
        return ResponseEntity.created(URI.create("/tours/" + tourResponseDTO.getId())).body(tourResponseDTO.getId());
    }

    @PostMapping("/optimize")
    @Operation(summary = "Optimize tour", description = "Create an optimized tour based on available deliveries")
    public ResponseEntity<TourResponseDTO> optimizeTour(@RequestBody OptimizationRequestDTO optimizationRequestDTO) {
        logger.error("Request =>  {}", optimizationRequestDTO);
        TourResponseDTO optimizedTour = this.tourService.optimizeTour(optimizationRequestDTO);
        return ResponseEntity.ok(optimizedTour);
    }

   @DeleteMapping("/{id}")
   @Operation(summary = "Delete a tour", description = "Delete a tour by its ID")
    public ResponseEntity<Void> deleteTour(@PathVariable Long id) {
        this.tourService.deleteById(id);

        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update a tour", description = "Update the details of an existing tour by its ID")
    public ResponseEntity<Void> updateTour(@PathVariable Long id,
                                           @RequestBody TourRequestDTO dto) {

        this.tourService.updateTour(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Void> startTour(@PathVariable Long id) {
        this.tourService.startTour(id);
        return ResponseEntity.noContent().build();
    }
}
