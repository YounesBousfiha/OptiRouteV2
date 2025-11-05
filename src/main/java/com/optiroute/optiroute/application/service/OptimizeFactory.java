package com.optiroute.optiroute.application.service;

import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.domain.service.MediumProblemRule;
import com.optiroute.optiroute.domain.service.OptimizerSelectionRule;
import com.optiroute.optiroute.domain.service.SmallProblemRule;
import com.optiroute.optiroute.infrastructure.strategy.TourOptimizer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OptimizeFactory {

    private final List<OptimizerSelectionRule> rules;

    public OptimizeFactory() {
        this.rules = List.of(
                new SmallProblemRule(),
                new MediumProblemRule()
        );
    }


    public TourOptimizer createOptimize(List<Delivery> deliveryList) {
        for(OptimizerSelectionRule rule: rules) {
            Optional<TourOptimizer> optimizer = rule.getOptimizer(deliveryList);
            if(optimizer.isPresent()) {
                return optimizer.get();
            }
        }
        return null;
    }
}
