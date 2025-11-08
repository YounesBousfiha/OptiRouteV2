package com.optiroute.optiroute.domain.service;

import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.infrastructure.strategy.ClarkeWrightOptimizer;
import com.optiroute.optiroute.infrastructure.strategy.TourOptimizer;

import java.util.List;
import java.util.Optional;


public class MediumProblemRule implements OptimizerSelectionRule{

    @Override
    public Optional<TourOptimizer> getOptimizer(List<Delivery> deliveryList) {
        if(deliveryList.size() >= 20 && deliveryList.size() <= 50) {
            return Optional.of(new ClarkeWrightOptimizer());
        }

        return Optional.empty();
    }
}
