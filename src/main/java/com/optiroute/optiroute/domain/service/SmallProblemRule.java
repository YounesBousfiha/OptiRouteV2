package com.optiroute.optiroute.domain.service;

import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.infrastructure.strategy.NearestNeighborOptimizer;
import com.optiroute.optiroute.infrastructure.strategy.TourOptimizer;

import java.util.List;
import java.util.Optional;

// TODO: add more Conditions Here
public class SmallProblemRule implements OptimizerSelectionRule {

    @Override
    public Optional<TourOptimizer> getOptimizer(List<Delivery> deliveryList) {
        if(!deliveryList.isEmpty() && deliveryList.size() < 20) {
            return Optional.of(new NearestNeighborOptimizer());
        }

        return Optional.empty();
    }
}
