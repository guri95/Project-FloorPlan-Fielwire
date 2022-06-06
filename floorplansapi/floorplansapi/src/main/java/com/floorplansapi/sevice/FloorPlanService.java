package com.floorplansapi.sevice;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.floorplansapi.entity.FloorPlan;
import com.floorplansapi.response.ProjectAndFloorPlanApiResponse;

public interface FloorPlanService {
	public ResponseEntity<ProjectAndFloorPlanApiResponse> deleteFloorPlanByid(Long id);
	public List<FloorPlan> getFloorPlanListByProjectId(Long id);
	public FloorPlan getFloorPlanById(Long id);
	public ResponseEntity<ProjectAndFloorPlanApiResponse> addFloorPlan(MultipartFile file, String floorPlanName) throws IOException;
	public ResponseEntity<ProjectAndFloorPlanApiResponse> updateFloorPlan(MultipartFile file, String floorPlanName,
			Long floorPlanId) throws IOException;
	public ResponseEntity<ProjectAndFloorPlanApiResponse> moveFloorPlanByProjectId(Long projectId, Long floorPlanId);
}
