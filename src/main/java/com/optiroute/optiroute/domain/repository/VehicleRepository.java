package com.optiroute.optiroute.domain.repository;

import com.optiroute.optiroute.domain.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VehicleRepository  extends JpaRepository<Vehicule, Long> {
}
