package com.optiroute.optiroute.application.service;

import com.optiroute.optiroute.application.mapper.DeliveryMapper;
import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.domain.entity.Tour;
import com.optiroute.optiroute.domain.enums.DeliveryStatus;
import com.optiroute.optiroute.domain.repository.DeliveryRepository;
import com.optiroute.optiroute.domain.repository.TourRepository;
import com.optiroute.optiroute.domain.vo.Coordinates;
import com.optiroute.optiroute.infrastructure.logging.AppLogger;
import com.optiroute.optiroute.presentation.dto.request.DeliveryRequestDTO;
import com.optiroute.optiroute.presentation.dto.response.DeliveryResponseDTO;
import org.slf4j.Logger;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DeliveryService {

    private final Logger logger = AppLogger.getLogger(DeliveryService.class);
    private DeliveryRepository deliveryRepository;
    private final TourRepository tourRepository;

    public DeliveryService(DeliveryRepository deliveryRepository, TourRepository tourRepository) {
        this.tourRepository = tourRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public List<DeliveryResponseDTO> getAllDeliveries() {
        List <Delivery> deliveries = this.deliveryRepository.findAll();

        return deliveries.stream()
                .map(DeliveryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public DeliveryResponseDTO getDeliveryById(Long id) {
        Delivery delivery = this.deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        return DeliveryMapper.toResponseDTO(delivery);
    }

    public DeliveryResponseDTO createDelivery(DeliveryRequestDTO dto) {

        dto.setUuid(UUID.randomUUID());
        dto.setDeliveryStatus(DeliveryStatus.PENDING);
        Delivery delivery = DeliveryMapper.toEntity(dto);
        Delivery savedDelivery = this.deliveryRepository.save(delivery);
        return DeliveryMapper.toResponseDTO(savedDelivery);
    }

    public void updateDelivery(Long id, DeliveryRequestDTO dto) {
        Delivery existingDelivery = this.deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        existingDelivery.setCoordinates(new Coordinates(dto.getLongitude(), dto.getLatitude()));
        existingDelivery.setPoids(dto.getPoids());
        existingDelivery.setVolume(dto.getVolume());

        this.deliveryRepository.save(existingDelivery);
    }

    public void deleteDelivery(Long id) {
        Delivery existingDelivery = this.deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        this.deliveryRepository.delete(existingDelivery);
    }


}
