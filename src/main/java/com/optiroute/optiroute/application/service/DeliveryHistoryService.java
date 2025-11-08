package com.optiroute.optiroute.application.service;

import com.optiroute.optiroute.application.mapper.ComplexResponseMapper;
import com.optiroute.optiroute.domain.entity.Customer;
import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.domain.entity.DeliveryHistory;
import com.optiroute.optiroute.domain.entity.Tour;
import com.optiroute.optiroute.domain.event.TourCompletedEvent;
import com.optiroute.optiroute.domain.repository.DeliveryHistoryRepository;
import com.optiroute.optiroute.presentation.dto.response.ComplexResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DeliveryHistoryService {

    private final DeliveryHistoryRepository deliveryHistoryRepository;


    @Autowired
    public DeliveryHistoryService(DeliveryHistoryRepository deliveryHistoryRepository) {
        this.deliveryHistoryRepository = deliveryHistoryRepository;
    }

    public List<ComplexResponseDTO> getComplexData(long delayInMinutes, int limit, int offset) {
        List<DeliveryHistory> deliveryHistories = this.deliveryHistoryRepository.findComplexData(delayInMinutes, limit, offset);

        return deliveryHistories.stream()
                .map(ComplexResponseMapper::toDTO)
                .toList();
    }

    @EventListener
    @Async
    public void handleTourCompleted(TourCompletedEvent event) {
        Tour tour = event.getCompletedTour();

        log.info("CONSUMER: Event Received for Tour ID : {}", tour.getId());

        List<DeliveryHistory> deliveryHistoryList = new ArrayList<>();

        for (Delivery delivery : tour.getDeliveryList()) {
            LocalDateTime actual= LocalDateTime.now();
            LocalDateTime planned = delivery.getPlannedDeliveryTime();
            long delayInMinutes = Duration.between(planned, actual).toMinutes();

            DeliveryHistory deliveryHistory = DeliveryHistory.builder()
                    .delivery(delivery)
                    .actualDeliveryTime(actual)
                    .plannedDeliveryTime(planned)
                    .delayInMinutes(delayInMinutes)
                    .dayOfWeek(actual.getDayOfWeek())
                    .build();

            deliveryHistoryList.add(deliveryHistory);
        }

        deliveryHistoryRepository.saveAll(deliveryHistoryList);
    }
}
