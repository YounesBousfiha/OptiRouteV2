package com.optiroute.optiroute.domain.service;

import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.infrastructure.strategy.ClarkeWrightOptimizer;
import com.optiroute.optiroute.infrastructure.strategy.TourOptimizer;

import java.util.List;
import java.util.Optional;


public class MediumProblemRule implements OptimizerSelectionRule{

    private final ClarkeWrightOptimizer optimizer;

    public MediumProblemRule(ClarkeWrightOptimizer optimizer) {
        this.optimizer = optimizer;
    }
    @Override
    public Optional<TourOptimizer> getOptimizer(List<Delivery> deliveryList) {
        if(deliveryList.size() >= 11 && deliveryList.size() <= 25) {
            return Optional.of(this.optimizer);
        }

        return Optional.empty();
    }
}
