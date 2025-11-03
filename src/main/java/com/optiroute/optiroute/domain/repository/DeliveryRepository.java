package com.optiroute.optiroute.domain.repository;

import com.optiroute.optiroute.domain.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("SELECT d FROM Delivery d " +
            "WHERE d.warehouse.id = :warehouseId " +
            "AND d.deliveryStatus =  com.optiroute.optiroute.domain.enums.DeliveryStatus.PENDING " +
            "AND d.isOptimized = false"
    )
    List<Delivery> findPendingDeliveriesForWarehouse(
            @Param("warehouseId") Long warehouseId
    );
}
