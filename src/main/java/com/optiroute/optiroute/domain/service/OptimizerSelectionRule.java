package com.optiroute.optiroute.domain.service;


import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.infrastructure.strategy.TourOptimizer;

import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface OptimizerSelectionRule {
    Optional<TourOptimizer> getOptimizer(List<Delivery> deliveryList);
}
