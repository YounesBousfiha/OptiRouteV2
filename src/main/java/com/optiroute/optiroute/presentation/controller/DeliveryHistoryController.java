package com.optiroute.optiroute.presentation.controller;

import com.optiroute.optiroute.application.service.DeliveryHistoryService;
import com.optiroute.optiroute.presentation.dto.response.ComplexResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery_history")
@RequiredArgsConstructor
public class DeliveryHistoryController {

    private final DeliveryHistoryService deliveryHistoryService;


    @GetMapping
    public ResponseEntity<List<ComplexResponseDTO>> getComplexData(
            @RequestParam(defaultValue = "0") long delayInMinutes,
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        List<ComplexResponseDTO> complexResponseDTOS = this.deliveryHistoryService.getComplexData(delayInMinutes, limit, offset);

        return ResponseEntity.ok(complexResponseDTOS);
    }
}
