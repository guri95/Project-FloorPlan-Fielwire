package com.floorplansapi.sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.floorplansapi.repository.FloorPlanRepository;
import com.floorplansapi.repository.ProjectRepository;

@Service
public class CommonServices {
	@Autowired
	private FloorPlanRepository floorPlanRepository;

	@Autowired
	private ProjectRepository projectRepository;
	public  CommonServices() {
	}
	public boolean isProjectIdValid(Long id) {
		return projectRepository.findById(id).isPresent();
	}

	public boolean isFloorPlanIdValid(Long id) {
		return floorPlanRepository.findById(id).isPresent();
	}
}
