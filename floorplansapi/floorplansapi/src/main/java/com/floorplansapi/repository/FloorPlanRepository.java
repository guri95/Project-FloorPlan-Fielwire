package com.floorplansapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.floorplansapi.entity.FloorPlan;

@Repository
public interface FloorPlanRepository extends JpaRepository<FloorPlan, Long> {

}
