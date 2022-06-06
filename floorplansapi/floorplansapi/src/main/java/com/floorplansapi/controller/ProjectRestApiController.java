package com.floorplansapi.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.floorplansapi.entity.Project;
import com.floorplansapi.imageprocessor.ImageController;
import com.floorplansapi.response.ProjectAndFloorPlanApiResponse;
import com.floorplansapi.sevice.ProjectService;

@RestController
@RequestMapping("/project")
public class ProjectRestApiController {
	@Autowired
	private ProjectService projectService;

	@Autowired
	ImageController imageController;

	@PostMapping(value = "/add", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<ProjectAndFloorPlanApiResponse> addProject(
			@RequestPart(value = "files", required = false) List<MultipartFile> files,
			@RequestParam(value = "projectName", required = true) String projectName,
			@RequestParam(value = "floorPlanName", required = false) String floorPlanName) throws IOException {
		return projectService.addProject(projectName, files, floorPlanName);
	}

	/* updates projectName by projectId */
	@PutMapping("/updateProjectNameByProjectId")
	public ResponseEntity<ProjectAndFloorPlanApiResponse> updateProjectName(@RequestBody Project request) {
		return projectService.updateNameById(request);
	}

	 /* 
	 * @PatchMapping("/patch") public ResponseEntity<ProjectAndFloorPlanApiResponse>
	 * patchProject(@RequestBody Project request) { return
	 * projectService.updateNameById(request); }
	 */

	/*
	 * get all projects with floorplans linked with it. we limit the projects count
	 * by limit. default limit is set to 10
	 */
	@GetMapping("/getAll")
	public List<Project> getAllProjects(@RequestParam(value = "limit", required = false) Long limit) {
		return projectService.getLimitedList(limit);
	}

	/* get project and floorplan by projectId */
	@GetMapping("/getbyid")
	public List<Project> getProjectById(@RequestParam(value = "id", required = false) Long id) {
		return projectService.getById(id);
	}

	/*
	 * remove project including all floorplans linked with it by projectId
	 */
	@DeleteMapping("/removeById")
	public ResponseEntity<ProjectAndFloorPlanApiResponse> removeById(
			@RequestParam(value = "id", required = false) Long id) {
		return projectService.removeById(id);
	}

	/*
	 * add new floorplan by projectId floorplan may or may not contain images. if
	 * floorplan under projectId already exists then add new floorplan to already
	 * existing floorplan list. we can add multiple floorplans at once by projectId
	 */
	@PostMapping("/addNewFloorPlansByProjectId")
	public ResponseEntity<ProjectAndFloorPlanApiResponse> addNewFloorPlansByProjectId(
			@RequestParam(value = "projectId", required = true) Long projectId,
			@RequestPart(value = "file", required = false) List<MultipartFile> files,
			@RequestParam(value = "floorPlanName", required = true) String floorPlanNames) throws IOException {
		return projectService.addNewFloorPlanByProjectId(projectId, files, floorPlanNames);
	}

}
