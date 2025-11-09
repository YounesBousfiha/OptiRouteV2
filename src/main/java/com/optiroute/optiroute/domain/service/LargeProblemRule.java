package com.optiroute.optiroute.domain.service;


import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.infrastructure.strategy.AIOptimizer;
import com.optiroute.optiroute.infrastructure.strategy.TourOptimizer;

import java.util.List;
import java.util.Optional;

public class LargeProblemRule implements OptimizerSelectionRule {

    private final AIOptimizer optimizer;

    public LargeProblemRule(AIOptimizer optimizer) {
        this.optimizer = optimizer;
    }

    @Override
    public Optional<TourOptimizer> getOptimizer(List<Delivery> deliveryList) {

        if(deliveryList.size() >= 26) {
            return Optional.of(this.optimizer);
        }

        return Optional.empty();
    }
}
