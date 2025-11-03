package com.optiroute.optiroute.application.service;

import com.optiroute.optiroute.application.mapper.WareHouseMapper;
import com.optiroute.optiroute.domain.entity.WareHouse;
import com.optiroute.optiroute.domain.repository.WareHouseRepository;
import com.optiroute.optiroute.infrastructure.logging.AppLogger;
import com.optiroute.optiroute.presentation.dto.request.WareHouseRequestDTO;
import com.optiroute.optiroute.presentation.dto.response.WareHouseResponseDTO;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WareHouseService {

    private final WareHouseRepository wareHouseRepository;

    private final Logger logger = AppLogger.getLogger(WareHouseService.class);

    public WareHouseService(WareHouseRepository wareHouseRepository) {
        this.wareHouseRepository = wareHouseRepository;
    }


    // TODO: Error handling & Logs
    public WareHouseResponseDTO createWareHouse(WareHouseRequestDTO dto) {

        WareHouse newWareHouse = WareHouseMapper.toEntity(dto);

        WareHouse savedWareHouse = this.wareHouseRepository.save(newWareHouse);

        return WareHouseMapper.toResponseDTO(savedWareHouse);

    }

    public List<WareHouseResponseDTO> getAllWareHouses() {
        List<WareHouse> wareHouseList = this.wareHouseRepository.findAll();
        if(null == wareHouseList || wareHouseList.isEmpty()) {
            logger.error("No WareHouses found in the repository.");
            return Collections.emptyList();
        }

        return wareHouseList.stream()
                .filter(Objects::nonNull)
                .map(WareHouseMapper::toResponseDTO)
                .collect(Collectors.toList());

    }

    public WareHouseResponseDTO getWareHouseById(Long id) {
        WareHouse wareHouse = this.wareHouseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Warehouse not found with id: {} ", id);
                    return new RuntimeException("Warehouse not found with id: " + id);
                });

        return WareHouseMapper.toResponseDTO(wareHouse);
    }

    // TODO: Error handling & Logs
    public void deleteWareHouse(Long id) {
        this.wareHouseRepository.deleteById(id);
    }


    public void updateWareHouse(Long id, WareHouseRequestDTO dto) {
        WareHouse wareHouse = this.wareHouseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Warehouse not found with id: {} ", id);
                    return new RuntimeException("Warehouse not found with id: " + id);
                });

        wareHouse.setOpenHour(dto.getOpenHour());
        wareHouse.setCloseHour(dto.getCloseHour());
        wareHouse.setAddress(dto.getAddress());

        this.wareHouseRepository.save(wareHouse);
    }
}
