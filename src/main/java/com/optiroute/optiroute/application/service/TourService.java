package com.optiroute.optiroute.application.service;

import com.optiroute.optiroute.application.mapper.TourMapper;
import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.domain.entity.Tour;
import com.optiroute.optiroute.domain.entity.Vehicule;
import com.optiroute.optiroute.domain.entity.WareHouse;
import com.optiroute.optiroute.domain.enums.DeliveryStatus;
import com.optiroute.optiroute.domain.event.TourCompletedEvent;
import com.optiroute.optiroute.domain.repository.DeliveryRepository;
import com.optiroute.optiroute.domain.repository.TourRepository;
import com.optiroute.optiroute.domain.repository.VehicleRepository;
import com.optiroute.optiroute.domain.repository.WareHouseRepository;
import com.optiroute.optiroute.infrastructure.strategy.TourOptimizer;
import com.optiroute.optiroute.presentation.dto.request.OptimizationRequestDTO;
import com.optiroute.optiroute.presentation.dto.request.TourRequestDTO;
import com.optiroute.optiroute.presentation.dto.response.TourResponseDTO;
import com.optiroute.optiroute.utility.HaversineUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TourService {

    private final TourRepository tourRepository;
    private final VehicleRepository vehicleRepository;
    private final WareHouseRepository wareHouseRepository;
    private final DeliveryRepository deliveryRepository;
    private final  OptimizeFactory optimizeFactory;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public TourService(TourRepository tourRepository,
                       VehicleRepository vehicleRepository,
                       WareHouseRepository wareHouseRepository,
                       DeliveryRepository deliveryRepository,
                       OptimizeFactory optimizeFactory,
                       ApplicationEventPublisher eventPublisher) {
        this.vehicleRepository = vehicleRepository;
        this.wareHouseRepository = wareHouseRepository;
        this.tourRepository = tourRepository;
        this.deliveryRepository = deliveryRepository;
        this.optimizeFactory = optimizeFactory;
        this.eventPublisher = eventPublisher;
    }



    public TourResponseDTO optimizeTour(OptimizationRequestDTO optimizationRequestDTO) {

        WareHouse wareHouse = this.wareHouseRepository.findById(optimizationRequestDTO.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        Vehicule vehicule = this.vehicleRepository.findById(optimizationRequestDTO.getVehiculeId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        List<Delivery> allPendingDeliveries = this.deliveryRepository.findPendingDeliveriesForWarehouse(wareHouse.getId());

        List<Delivery> deliveriesToOptimize = this.filterByVehiculeCapacity(allPendingDeliveries, vehicule);


        TourOptimizer optimizer = this.optimizeFactory.createOptimize(deliveriesToOptimize);

        if(null == optimizer) {
            throw new RuntimeException("No suitable optimizer found for the given deliveries");
        }

        List<Delivery> optimizedDeliveries = optimizer.calculateOptimalTour(wareHouse, deliveriesToOptimize, vehicule);

        // Make sure to update the Delivery Status
        Tour optimizedTour = Tour.builder()
                .vehicule(vehicule)
                .wareHouse(wareHouse)
                .deliveryList(optimizedDeliveries)
                .build();

        double totalDistance = calculateTourDistance(optimizedTour);

        optimizedTour.setTotalDistance(totalDistance);

        Tour savedTour = this.tourRepository.save(optimizedTour);

        optimizedDeliveries.forEach(delivery -> {
            delivery.setTour(savedTour);
            delivery.setDeliveryStatus(DeliveryStatus.IN_TRANSIT);
            delivery.setIsOptimized(true);
        });

        this.deliveryRepository.saveAll(optimizedDeliveries);

        return TourMapper.tourResponseDTO(savedTour);
    }

    private double calculateTourDistance(Tour tour) {
        if(null == tour || null == tour.getDeliveryList() || tour.getDeliveryList().isEmpty()) {
            return 0.0;
        }

        double totalDistance = 0.0;
        WareHouse wareHouse = tour.getWareHouse();

        double prevLat = wareHouse.getCoordinates().latitude();
        double prevLon = wareHouse.getCoordinates().longitude();

        for(Delivery delivery : tour.getDeliveryList()) {
            double currentLat = delivery.getCoordinates().latitude();
            double currentLon = delivery.getCoordinates().longitude();

            totalDistance += HaversineUtil.calculateDistance(prevLat, prevLon, currentLat, currentLon);

            prevLat = currentLat;
            prevLon = currentLon;
        }

        totalDistance += HaversineUtil.calculateDistance(prevLat,
                prevLon,
                wareHouse.getCoordinates().latitude(),
                wareHouse.getCoordinates().longitude()
        );

        return totalDistance;
    }


    public List<TourResponseDTO> getAllTours() {
        List <Tour> tours = this.tourRepository.findAll();

        return tours.stream()
                .map(TourMapper::tourResponseDTO)
                .collect(Collectors.toList());
    }


   public TourResponseDTO createTour(TourRequestDTO dto) {
       Vehicule vehicule = this.vehicleRepository.findById(dto.getVehiculeId())
               .orElseThrow(() -> new RuntimeException("Vehicule not found"));

       WareHouse wareHouse = this.wareHouseRepository.findById(dto.getWareHouseId())
               .orElseThrow(() -> new RuntimeException("WareHouse not found"));


         dto.setVehicule(vehicule);
         dto.setWareHouse(wareHouse);

         Tour tour = TourMapper.toEntity(dto);

        Tour newTour = this.tourRepository.save(tour);

        return TourMapper.tourResponseDTO(newTour);
    }

    public void deleteById(Long id) {
        this.tourRepository.deleteById(id);
    }

    public void updateTour(Long id, TourRequestDTO dto) {

        Tour oldTour = this.tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found"));
        Vehicule vehicule = this.vehicleRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new RuntimeException("Vehicule not found"));
        WareHouse wareHouse = this.wareHouseRepository.findById(dto.getWareHouseId())
                .orElseThrow(() -> new RuntimeException("WareHouse not found"));

        oldTour.setVehicule(vehicule);
        oldTour.setWareHouse(wareHouse);

        this.tourRepository.save(oldTour);
    }

    public Optional<Tour> findById(Long id) {
        return this.tourRepository.findById(id);
    }


    public void startTour(Long id) {
        Tour tour = this.tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        List<Delivery> deliveries = tour.getDeliveryList();
        for(Delivery delivery : deliveries) {
            delivery.setDeliveryStatus(DeliveryStatus.DELIVERED);
            delivery.setActualDeliveryTime(LocalDateTime.now());
        }
        eventPublisher.publishEvent(new TourCompletedEvent(tour));

        this.deliveryRepository.saveAll(deliveries);
    }

    private List<Delivery> filterByVehiculeCapacity(List<Delivery> deliveries, Vehicule vehicule) {
        List<Delivery> selectedDeliveries = new ArrayList<>();
        double currentWeight = 0.0;
        double currentVolume = 0.0;
        double maxWeight = vehicule.getVehicleType().getMaxWeightKg();
        double maxVolume = vehicule.getVehicleType().getMaxVolumeM3();

        for(Delivery delivery : deliveries) {
            double deliveryWeight = delivery.getPoids();
            double deliveryVolume = delivery.getVolume();

            if(currentWeight + deliveryWeight <= maxWeight && currentVolume + deliveryVolume <= maxVolume) {
                selectedDeliveries.add(delivery);
                currentVolume += deliveryVolume;
                currentWeight += deliveryWeight;
            }
        }

        return selectedDeliveries;
    }
}
