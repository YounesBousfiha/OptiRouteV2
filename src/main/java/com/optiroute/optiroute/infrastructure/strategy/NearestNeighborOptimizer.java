package com.optiroute.optiroute.infrastructure.strategy;

import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.domain.entity.Vehicule;
import com.optiroute.optiroute.domain.entity.WareHouse;
import com.optiroute.optiroute.domain.enums.VehicleType;
import com.optiroute.optiroute.presentation.dto.request.OptimizationRequestDTO;
import com.optiroute.optiroute.presentation.dto.request.WareHouseRequestDTO;
import com.optiroute.optiroute.utility.HaversineUtil;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class NearestNeighborOptimizer implements TourOptimizer {
    @Override
    public List<Delivery> calculateOptimalTour(WareHouse wareHouse, List<Delivery> deliveryList, Vehicule vehicule) {

        if (null == deliveryList || deliveryList.isEmpty()) {
            return new ArrayList<>();
        }

        // Extract Vehicule Specification
        VehicleType vehicleType = vehicule.getVehicleType();
        double maxWeightKg = vehicleType.getMaxWeightKg();
        double maxVolumeM3 = vehicleType.getMaxVolumeM3();

        // Put all PENDING Delivery in WareHouse Into this list
        List<Delivery> unvisitedDeliveries = new ArrayList<>(deliveryList);

        // Create a new array for the optimized Tour
        List<Delivery> optimizedTour = new ArrayList<>();

        // Extract the WareHouse Coordinates
        double currentLat = wareHouse.getCoordinates().latitude();
        double currentLon = wareHouse.getCoordinates().longitude();

        // Variables to track if we reach the Vehicule capacity
        double currentWeightKg = 0;
        double currentVolumeM3 = 0;


        while (!unvisitedDeliveries.isEmpty()) {

            if(this.isVehiculeReachMax(maxWeightKg, maxVolumeM3, currentWeightKg, currentVolumeM3)) {
                break;
            }

            Optional<Delivery>  nearestDelivery = findNearestDelivery(
                    currentLat, currentLon,
                    unvisitedDeliveries,
                    currentWeightKg, currentVolumeM3,
                    maxWeightKg, maxVolumeM3
            );

            if (nearestDelivery.isEmpty()) {
                break;
            }

            Delivery selectedDelivery = nearestDelivery.get();

            optimizedTour.add(selectedDelivery);
            currentLat = selectedDelivery.getCoordinates().latitude();
            currentLon = selectedDelivery.getCoordinates().longitude();

            currentWeightKg += selectedDelivery.getPoids();
            currentVolumeM3 += selectedDelivery.getVolume();

            unvisitedDeliveries.remove(selectedDelivery);
        }

        return optimizedTour;
    }

    private Optional<Delivery> findNearestDelivery(
            double currentLat,
            double currentLon,
            List<Delivery> unvisitedDeliveries,
            double currentWeightKg,
            double currentVolumeM3,
            double maxWeightKg,
            double maxVolumeM3
    ) {
        return unvisitedDeliveries.stream()
                .filter(delivery ->
                                (currentWeightKg + delivery.getPoids() <= maxWeightKg) &&
                                (currentVolumeM3 + delivery.getVolume() <= maxVolumeM3)
                        )
                .min(Comparator.comparingDouble(delivery ->
                        HaversineUtil.calculateDistance(
                                currentLat,
                                currentLon,
                                delivery.getCoordinates().latitude(),
                                delivery.getCoordinates().longitude()
                        ))
                );
    }

    private  boolean isVehiculeReachMax(
            double maxWeightKg,
            double maxVolumeM3,
            double currentWeightKg,
            double currentVolumeM3
    ) {
        boolean isOverWeight = currentWeightKg >= maxWeightKg * 0.95;
        boolean isOverCapacity = currentVolumeM3 >= maxVolumeM3 * 0.95;

        return isOverWeight || isOverCapacity;
    }
}
