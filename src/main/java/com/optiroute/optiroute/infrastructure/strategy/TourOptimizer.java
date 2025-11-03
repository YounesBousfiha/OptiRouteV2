package com.optiroute.optiroute.infrastructure.strategy;

import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.domain.entity.Vehicule;
import com.optiroute.optiroute.domain.entity.WareHouse;
import java.util.List;

public interface TourOptimizer {
    List<Delivery> calculateOptimalTour(WareHouse wareHouse, List<Delivery> deliveryList, Vehicule vehicule);
}
