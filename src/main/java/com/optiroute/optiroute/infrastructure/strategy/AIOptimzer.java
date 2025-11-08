package com.optiroute.optiroute.infrastructure.strategy;

import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.domain.entity.Vehicule;
import com.optiroute.optiroute.domain.entity.WareHouse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name = "optimizer.strategy", havingValue = "ai", matchIfMissing = true)
public class AIOptimzer implements TourOptimizer {
    @Override
    public List<Delivery> calculateOptimalTour(WareHouse wareHouse, List<Delivery> deliveryList, Vehicule vehicule) {
        return List.of();
    }
}
