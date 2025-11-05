package com.optiroute.optiroute.domain.repository;

import com.optiroute.optiroute.domain.entity.Tour;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    @EntityGraph(attributePaths = {"deliveries", "wareHouse", "vehicule"})
    Optional<Tour> findWithAllRelationsById(Long id);
}
