package com.optiroute.optiroute.domain.repository;

import com.optiroute.optiroute.domain.entity.WareHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WareHouseRepository  extends JpaRepository<WareHouse, Long>{
}
