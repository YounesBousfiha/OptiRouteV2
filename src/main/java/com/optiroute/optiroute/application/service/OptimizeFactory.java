package com.optiroute.optiroute.application.service;

import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.domain.service.LargeProblemRule;
import com.optiroute.optiroute.domain.service.MediumProblemRule;
import com.optiroute.optiroute.domain.service.OptimizerSelectionRule;
import com.optiroute.optiroute.domain.service.SmallProblemRule;
import com.optiroute.optiroute.infrastructure.strategy.AIOptimizer;
import com.optiroute.optiroute.infrastructure.strategy.ClarkeWrightOptimizer;
import com.optiroute.optiroute.infrastructure.strategy.NearestNeighborOptimizer;
import com.optiroute.optiroute.infrastructure.strategy.TourOptimizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OptimizeFactory {

    private final List<OptimizerSelectionRule> rules;
    private final NearestNeighborOptimizer nearestNeighborOptimizer;
    private final ClarkeWrightOptimizer clarkeWrightOptimizer;
    private final AIOptimizer aiOptimizer;

    @Autowired
    public OptimizeFactory(
            NearestNeighborOptimizer nearestNeighborOptimizer,
            ClarkeWrightOptimizer clarkeWrightOptimizer,
            AIOptimizer aiOptimizer
    ) {

        this.nearestNeighborOptimizer = nearestNeighborOptimizer;
        this.clarkeWrightOptimizer = clarkeWrightOptimizer;
        this.aiOptimizer = aiOptimizer;
        this.rules = List.of(
                new SmallProblemRule(nearestNeighborOptimizer),
                new MediumProblemRule(clarkeWrightOptimizer),
                new LargeProblemRule(aiOptimizer)
        );
    }


    public TourOptimizer createOptimize(List<Delivery> deliveryList) {
        return rules.stream()
                .map(rule -> rule.getOptimizer(deliveryList))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElse(null);
    }
}
