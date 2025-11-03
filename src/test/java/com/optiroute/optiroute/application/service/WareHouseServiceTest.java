package com.optiroute.optiroute.application.service;


import com.optiroute.optiroute.application.mapper.WareHouseMapper;
import com.optiroute.optiroute.domain.entity.WareHouse;
import com.optiroute.optiroute.domain.repository.WareHouseRepository;
import com.optiroute.optiroute.domain.vo.Address;
import com.optiroute.optiroute.domain.vo.Coordinates;
import com.optiroute.optiroute.presentation.dto.request.WareHouseRequestDTO;
import com.optiroute.optiroute.presentation.dto.response.WareHouseResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WareHouseServiceTest {

    private WareHouseService wareHouseService;

    @Mock
    private WareHouseRepository wareHouseRepository;

    @BeforeEach
    void setuUp() {
        wareHouseService = new WareHouseService(wareHouseRepository);
    }

    private WareHouse createSampleWareHouse() {
        WareHouse wareHouse = new WareHouse();
        wareHouse.setId(1L);
        wareHouse.setOpenHour(LocalTime.of(8, 0));
        wareHouse.setCloseHour(LocalTime.of(18, 0));
        wareHouse.setAddress(new Address("123 Test St", "Test City", "Test Country", "20000"));
        wareHouse.setCoordinates(new Coordinates(10.0, 20.0));
        return wareHouse;
    }

    private WareHouseRequestDTO createSampleWareHouseRequestDTO() {
        return WareHouseRequestDTO.builder()
                .openHour(LocalTime.of(8, 0))
                .closeHour(LocalTime.of(18, 0))
                .address(new Address("123 Test St", "Test City", "Test Country", "20000"))
                .coordinates(new Coordinates(10.0, 20.0))
                .build();
    }

    @Test
    void createWareHouse_shouldReturnResponseDTO() {
        WareHouseRequestDTO wareHouseRequestDTO = createSampleWareHouseRequestDTO();
        WareHouse wareHouse = WareHouseMapper.toEntity(wareHouseRequestDTO);

        when(wareHouseRepository.save(any(WareHouse.class))).thenReturn(wareHouse);

        WareHouseResponseDTO wareHouseResponseDTO = wareHouseService.createWareHouse(wareHouseRequestDTO);

        assertNotNull(wareHouseResponseDTO);
        assertEquals(wareHouseRequestDTO.getOpenHour(), wareHouseResponseDTO.getOpenHour());
        assertEquals(wareHouseRequestDTO.getCloseHour(), wareHouseResponseDTO.getCloseHour());
        verify(wareHouseRepository).save(any(WareHouse.class));
    }

    @Test
    void getAllWareHouses_whenWarehousesExist_shouldReturnList() {
        WareHouse wareHouse = createSampleWareHouse();
        when(wareHouseRepository.findAll()).thenReturn(List.of(wareHouse));

        List<WareHouseResponseDTO> responseDTOS = wareHouseService.getAllWareHouses();

        assertFalse(responseDTOS.isEmpty());
        assertEquals(1, responseDTOS.size());
        verify(wareHouseRepository).findAll();
    }

    @Test
    void getAllWareHouses_whenNoWareHouseExist_shouldReturnEmptyList() {
        when(wareHouseRepository.findAll()).thenReturn(Collections.emptyList());


        List<WareHouseResponseDTO> responseDTOS = wareHouseService.getAllWareHouses();

        assertTrue(responseDTOS.isEmpty());
        verify(wareHouseRepository).findAll();
    }


}
