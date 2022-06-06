package com.floorplansapi.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.floorplansapi.entity.FloorPlan;
import com.floorplansapi.response.ProjectAndFloorPlanApiResponse;
import com.floorplansapi.sevice.FloorPlanService;

@RestController
@RequestMapping("/floorplan")
public class FloorPlanRestApiController {
	
	@Autowired
	private FloorPlanService floorPlanService;
	
	/*
	 * @PostMapping("/addFloorPlan") public FloorPlan addProject(@RequestBody
	 * FloorPlan floorPlan) { return floorPlanRepository.save(floorPlan); }
	 * 
	 * @PatchMapping("/patchfloorPlan") public FloorPlan patchProject(@RequestBody
	 * FloorPlan floorPlan) { return floorPlanRepository.save(floorPlan); }
	 * 
	 * @PutMapping("/updatefloorPlan") public FloorPlan updateProject(@RequestBody
	 * FloorPlan floorPlan) { return floorPlanRepository.save(floorPlan); }
	 */
	
	@DeleteMapping("/removeProjectById")
	public ResponseEntity<ProjectAndFloorPlanApiResponse> removeProject(@RequestParam(value = "id", required = false) Long id) {
		return floorPlanService.deleteFloorPlanByid(id);
	}
	
	@GetMapping("/getFloorPlanById")
	public FloorPlan getFloorPlanById(@RequestParam(value = "id", required = false) Long id) {
		return floorPlanService.getFloorPlanById(id);
	}
	
	@GetMapping("/getFloorPlansByProjectId")
	public List<FloorPlan> getFloorPlanByProjectId(@RequestParam(value = "id", required = false) Long id) {
		
		return floorPlanService.getFloorPlanListByProjectId(id);
	}
	
	@PostMapping("/addFloorPlan")
	public ResponseEntity<ProjectAndFloorPlanApiResponse> addFloorPlan(@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "floorPlanName", required = false) String floorPlanName) throws IOException{
		
		return floorPlanService.addFloorPlan(file, floorPlanName);
	}
	
	/* update already existing floorplan by floorplan id if floorplan id does
	 * not exists then create floorplan by floorplan id */
	@PutMapping("/updateFloorPlanByProjectId")
	public ResponseEntity<ProjectAndFloorPlanApiResponse> addFloorPlan(@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "floorPlanName", required = false) String floorPlanName,
			@RequestParam(value = "floorPlanId", required = true) Long floorPlanId) throws IOException{
		
		return floorPlanService.updateFloorPlan(file, floorPlanName, floorPlanId);
	}
	@PutMapping("/moveFloorPlanByProjectId")
	public ResponseEntity<ProjectAndFloorPlanApiResponse> setFloorPlanByProjectId(
			@RequestParam(value = "projectId", required = true) Long projectId,
			@RequestParam(value = "floorPlanId", required = true) Long floorPlanId){
		
		return floorPlanService.moveFloorPlanByProjectId(projectId, floorPlanId);
	}
}
