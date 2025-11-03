package com.optiroute.optiroute.infrastructure.strategy;

import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.domain.entity.Vehicule;
import com.optiroute.optiroute.domain.entity.WareHouse;
import com.optiroute.optiroute.utility.HaversineUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

public class ClarkeWrightOptimizer implements TourOptimizer {

    @Override
    public List<Delivery> calculateOptimalTour(WareHouse wareHouse, List<Delivery> deliveryList, Vehicule vehicule) {
        if (deliveryList == null || deliveryList.isEmpty()) {
            return List.of();
        }

        // If only one delivery, return as is
        if (deliveryList.size() == 1) {
            return deliveryList;
        }

        // Step 1: Calculate savings for merging routes
        List<Saving> savings = calculateSavings(wareHouse, deliveryList);

        // Step 2: Sort savings in descending order
        savings.sort(Comparator.comparingDouble(Saving::getSavingsValue).reversed());

        // Step 3: Create initial routes (each delivery is a separate route)
        List<Route> routes = deliveryList.stream()
                .map(delivery -> new Route(wareHouse, delivery))
                .collect(Collectors.toList());

        // Step 4: Merge routes based on savings
        for (Saving saving : savings) {
            Route route1 = findRouteWithDelivery(routes, saving.getDelivery1());
            Route route2 = findRouteWithDelivery(routes, saving.getDelivery2());

            if (route1 != route2 && canMergeRoutes(route1, route2, vehicule)) {
                mergeRoutes(routes, route1, route2);
            }
        }

        // Return the first route's deliveries (which now contains the merged optimal route)
        return routes.get(0).getDeliveries();
    }

    private List<Saving> calculateSavings(WareHouse wareHouse, List<Delivery> deliveryList) {
        List<Saving> savings = new ArrayList<>();
        double warehouseLat = wareHouse.getCoordinates().latitude();
        double warehouseLon = wareHouse.getCoordinates().longitude();

        for (int i = 0; i < deliveryList.size(); i++) {
            for (int j = i + 1; j < deliveryList.size(); j++) {
                Delivery delivery1 = deliveryList.get(i);
                Delivery delivery2 = deliveryList.get(j);

                double savingsValue = calculateSavingsValue(
                        warehouseLat, warehouseLon,
                        delivery1.getCoordinates().latitude(), delivery1.getCoordinates().longitude(),
                        delivery2.getCoordinates().latitude(), delivery2.getCoordinates().longitude()
                );

                savings.add(new Saving(delivery1, delivery2, savingsValue));
            }
        }

        return savings;
    }

    private double calculateSavingsValue(
            double warehouseLat, double warehouseLon,
            double lat1, double lon1,
            double lat2, double lon2
    ) {
        // S(i,j) = d(0,i) + d(0,j) - d(i,j)
        // 0 represents warehouse, i and j are delivery points
        double distToWarehouse1 = HaversineUtil.calculateDistance(warehouseLat, warehouseLon, lat1, lon1);
        double distToWarehouse2 = HaversineUtil.calculateDistance(warehouseLat, warehouseLon, lat2, lon2);
        double distBetweenDeliveries = HaversineUtil.calculateDistance(lat1, lon1, lat2, lon2);

        return distToWarehouse1 + distToWarehouse2 - distBetweenDeliveries;
    }

    private Route findRouteWithDelivery(List<Route> routes, Delivery delivery) {
        return routes.stream()
                .filter(route -> route.contains(delivery))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No route found for delivery"));
    }

    private boolean canMergeRoutes(Route route1, Route route2, Vehicule vehicule) {
        // Check total weight and volume constraints
        double totalWeight = route1.getTotalWeight() + route2.getTotalWeight();
        double totalVolume = route1.getTotalVolume() + route2.getTotalVolume();

        return totalWeight <= vehicule.getVehicleType().getMaxWeightKg() && totalVolume <= vehicule.getVehicleType().getMaxVolumeM3();
    }

    private void mergeRoutes(List<Route> routes, Route route1, Route route2) {
        route1.merge(route2);
        routes.remove(route2);
    }

    // Inner classes to help with route management
    private static class Saving {
        private final Delivery delivery1;
        private final Delivery delivery2;
        private final double savingsValue;

        public Saving(Delivery delivery1, Delivery delivery2, double savingsValue) {
            this.delivery1 = delivery1;
            this.delivery2 = delivery2;
            this.savingsValue = savingsValue;
        }

        public Delivery getDelivery1() { return delivery1; }
        public Delivery getDelivery2() { return delivery2; }
        public double getSavingsValue() { return savingsValue; }
    }

    @Data
    @AllArgsConstructor
    private static class Route {
        private final WareHouse wareHouse;
        private final List<Delivery> deliveries;

        public Route(WareHouse wareHouse, Delivery delivery) {
            this.wareHouse = wareHouse;
            this.deliveries = new ArrayList<>(List.of(delivery));
        }

        public boolean contains(Delivery delivery) {
            return deliveries.contains(delivery);
        }

        public void merge(Route other) {
            this.deliveries.addAll(other.deliveries);
        }

        public double getTotalWeight() {
            return deliveries.stream()
                    .mapToDouble(Delivery::getPoids)
                    .sum();
        }

        public double getTotalVolume() {
            return deliveries.stream()
                    .mapToDouble(Delivery::getVolume)
                    .sum();
        }
    }
}