package com.optiroute.optiroute.domain.service;

import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.infrastructure.strategy.NearestNeighborOptimizer;
import com.optiroute.optiroute.infrastructure.strategy.TourOptimizer;

import java.util.List;
import java.util.Optional;

public class SmallProblemRule implements OptimizerSelectionRule {
    private final NearestNeighborOptimizer optimizer;

    public SmallProblemRule(NearestNeighborOptimizer optimizer) {
        this.optimizer = optimizer;
    }

    @Override
    public Optional<TourOptimizer> getOptimizer(List<Delivery> deliveryList) {
        if(!deliveryList.isEmpty() && deliveryList.size() <= 10) {
            return Optional.of(this.optimizer);
        }

        return Optional.empty();
    }
}
