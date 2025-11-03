package com.optiroute.optiroute.presentation.controller;


import com.optiroute.optiroute.application.service.WareHouseService;
import com.optiroute.optiroute.presentation.dto.request.WareHouseRequestDTO;
import com.optiroute.optiroute.presentation.dto.response.WareHouseResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/warehouse")
public class WareHouseController {

    private final WareHouseService wareHouseService;


    public WareHouseController(WareHouseService wareHouseService) {
        this.wareHouseService = wareHouseService;
    }

    @GetMapping
    @Operation(
            summary = "Get all warehouses",
            description = "Retrieve a list of all warehouses",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved list of warehouses",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = WareHouseResponseDTO.class)
                            )
                    )
            }
    )
    public ResponseEntity<List<WareHouseResponseDTO>> getAllWareHouses() {
        List<WareHouseResponseDTO> wareHouseDTOList = this.wareHouseService.getAllWareHouses();
        return ResponseEntity.ok(wareHouseDTOList);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a warehouse by ID",
            description = "Retrieve the warehouse with the specified ID",
            parameters = {
                    @Parameter(
                            name = "id", description = "Unique identifier of the warehouse",
                            required = true, example = "1",
                            schema = @Schema(type = "long", example = "1")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the warehouse",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = WareHouseResponseDTO.class)
                            )
                    ),
                    @ApiResponse(ref = "#/components/responses/NotFound"),
                    @ApiResponse(ref = "#/components/responses/BadRequest"),
                    @ApiResponse(ref = "#/components/responses/InternalError")
            }
    )
    public ResponseEntity<WareHouseResponseDTO> getWareHouseById(@PathVariable Long id) {
        WareHouseResponseDTO wareHouseDTO = this.wareHouseService.getWareHouseById(id);
        return ResponseEntity.ok(wareHouseDTO);
    }

    @PostMapping
    @Operation(summary = "Create a new warehouse", description = "Create a new warehouse with the provided details")
    public ResponseEntity<Long> createWareHouse(@RequestBody WareHouseRequestDTO dto) {
        WareHouseResponseDTO wareHouseResponseDTO = this.wareHouseService.createWareHouse(dto);
        return ResponseEntity.created(URI.create("/warehouses/" + wareHouseResponseDTO.getId())).body(wareHouseResponseDTO.getId());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a warehouse", description = "Delete the warehouse with the specified ID")
    public ResponseEntity<Void> deleteWareHouse(@PathVariable Long id) {
        this.wareHouseService.deleteWareHouse(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a warehouse", description = "Update the details of the warehouse with the specified ID")
    public ResponseEntity<Void> updateWareHouse(@PathVariable Long id,
                                                @RequestBody WareHouseRequestDTO dto) {
        this.wareHouseService.updateWareHouse(id, dto);
        return ResponseEntity.noContent().build();
    }

}
