package com.optiroute.optiroute.domain.repository;

import com.optiroute.optiroute.domain.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository  extends JpaRepository<Vehicule, Long> {
}
