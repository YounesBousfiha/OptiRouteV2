package com.optiroute.optiroute.domain.repository;

import com.optiroute.optiroute.domain.entity.DeliveryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory, Long> {

    @Query("SELECT dh FROM DeliveryHistory dh JOIN FETCH dh.delivery d WHERE dh.delayInMinutes < :minDelay ORDER BY dh.plannedDeliveryTime DESC LIMIT :limit OFFSET :offset")
    List<DeliveryHistory> findComplexData(
            @Param("minDelay") long minDelay,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
