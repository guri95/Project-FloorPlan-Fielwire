package com.floorplansapi.sevice;


import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.floorplansapi.entity.Project;
import com.floorplansapi.response.ProjectAndFloorPlanApiResponse;


public interface ProjectService {
	public ResponseEntity<ProjectAndFloorPlanApiResponse> addProject(String projectName, List<MultipartFile> files, String floorPlanName) throws IOException;
	public ResponseEntity<ProjectAndFloorPlanApiResponse> updateNameById(Project request); 

	public List<Project> getLimitedList(Long limit);
	public List<Project> getById(Long id);
	public ResponseEntity<ProjectAndFloorPlanApiResponse> removeById(Long id);
	public ResponseEntity<ProjectAndFloorPlanApiResponse> addNewFloorPlanByProjectId(Long projectId, List<MultipartFile> listOfFloorplans, String floorNames) throws IOException;
	public ResponseEntity<ProjectAndFloorPlanApiResponse> moveFloorPlanToOtherProject(Long projectId, Long floorId);
}
