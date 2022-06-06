package com.floorplansapi.sevice;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.floorplansapi.constants.Constants;
import com.floorplansapi.constants.Messages;
import com.floorplansapi.entity.FloorPlan;
import com.floorplansapi.entity.Project;
import com.floorplansapi.exceptions.ProjectException;
import com.floorplansapi.imageprocessor.ImageController;
import com.floorplansapi.repository.FloorPlanRepository;
import com.floorplansapi.repository.ProjectRepository;
import com.floorplansapi.response.ProjectAndFloorPlanApiResponse;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private FloorPlanRepository floorPlanRepository;

	@Autowired
	ImageController imageController;

	@Override
	public ResponseEntity<ProjectAndFloorPlanApiResponse> updateNameById(Project request) {
		try {
			Long id = request.getId();
			if (isProjectIdValid(id)) {
				Project project = projectRepository.findById(id).get();
				project.setName(request.getName());
				projectRepository.save(project);
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ProjectAndFloorPlanApiResponse(Messages.NAME_UPDATED_SUCCESSFULLY + " id " + id));
			} else {
				Project newProject = new Project();
				newProject.setName(request.getName());
			}
		} catch (ProjectException e) {
			logger.error("Project request failed to insert in Database. {}", e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ProjectAndFloorPlanApiResponse(Messages.BAD_REQUEST));
	}

	@Override
	public List<Project> getLimitedList(Long limit) {
		List<Project> listOfProjects = projectRepository.findAll();

		List<Project> limitedList = new ArrayList<>();

		if (limit == null || limit > listOfProjects.size() || limit > 10000) {
			limit = (long) Constants.PROJECT_DEFAULT_LIMIT;
			--limit;
		}
		
		for (int i = 0; i < listOfProjects.size(); i++) {
			limitedList.add(listOfProjects.get(i));
			if (i == limit)
				break;
		}
		return limitedList;
	}

	@Override
	public List<Project> getById(Long id) {
		try {
			if (isProjectIdValid(id)) {
				List<Project> projectList = new ArrayList<>();
				projectList.add(projectRepository.findById(id).get());
				return projectList;
			}

			logger.error("Project id doestn't exists. id:{}", id);
		} catch (ProjectException e) {
			logger.error("Unable to retrieved Project by id {}", e.getMessage());
		}
		return Collections.emptyList();
	}

	@Override
	public ResponseEntity<ProjectAndFloorPlanApiResponse> removeById(Long id) {
		try {
			if (isProjectIdValid(id)) {
				List<FloorPlan> floorPlans = projectRepository.findById(id).get().getFloorPlan();
				if (floorPlans != null)
					floorPlans.forEach(floorPlan -> floorPlanRepository.delete(floorPlan));
				projectRepository.deleteById(id);
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ProjectAndFloorPlanApiResponse("Project removed Successfully: id " + id));
			}
			logger.error("Project id doestn't exists. id {}", id);
		} catch (ProjectException e) {
			logger.error("Unable to remove Project by id {}.{}", id, e);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ProjectAndFloorPlanApiResponse(Messages.BAD_REQUEST));
	}

	@Override
	public ResponseEntity<ProjectAndFloorPlanApiResponse> addNewFloorPlanByProjectId(Long projectId,
			List<MultipartFile> files, String floorNames) throws IOException {
		try {
			if (isProjectIdValid(projectId)) {

				Project project = projectRepository.findById(projectId).get();
				List<FloorPlan> floorPlanList = getFloorPlanList(project, files, floorNames);

				project.setFloorPlan(floorPlanList);
				projectRepository.save(project);
				return ResponseEntity.status(HttpStatus.OK).body(new ProjectAndFloorPlanApiResponse(
						"New FloorPlans added by project id: " + projectId));
			}
			logger.error("Project id doestn't exists. id {}", projectId);
		} catch (ProjectException e) {
			logger.error("Unable to add floorplan to Project table of project id {}.{}", projectId, e);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ProjectAndFloorPlanApiResponse(Messages.BAD_REQUEST));
	}

	public List<FloorPlan> getFloorPlanList(Project project, List<MultipartFile> files, String floorNames)
			throws IOException {
		List<FloorPlan> floorPlanList = new ArrayList<>();
		
		if(project.getFloorPlan() != null) {
			floorPlanList = project.getFloorPlan();
		}
		if (files != null && !files.isEmpty()) {
			List<String> ListOfFloorNames = new ArrayList<>();

			for (String floorplanName : floorNames.split(",")) {
				ListOfFloorNames.add(floorplanName);
			}

			int count = 0;
			for (MultipartFile file : files) {
				FloorPlan floorplan = new FloorPlan();
				byte[] fileBytes = file.getBytes();

				floorplan.setName(
						count < ListOfFloorNames.size() ? ListOfFloorNames.get(count++) : file.getOriginalFilename());
				floorplan.setOriginalImage(fileBytes);
				floorplan.setThum(imageController.compressMultiparFileImage(file, Constants.THUMBNAIL));
				floorplan.setLarge(imageController.compressMultiparFileImage(file, Constants.LARGEIMAGE));
				floorPlanList.add(floorplan);
			}
		} else {
			FloorPlan floorplan = new FloorPlan();
			floorplan.setName(floorNames);
			floorPlanList.add(floorplan);
		}

		return floorPlanList;
	}

	@Override
	public ResponseEntity<ProjectAndFloorPlanApiResponse> addProject(String projectName, List<MultipartFile> files,
			String floorPlanName) throws IOException {
		try {
			Project newProject = new Project();
			newProject.setName(projectName);

			if (files != null && !files.isEmpty()) {
				 List<FloorPlan> floorPlanList = getFloorPlanList(newProject, files, floorPlanName);
				if (floorPlanList == null)
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(new ProjectAndFloorPlanApiResponse(Messages.BAD_REQUEST));

				if (!floorPlanList.isEmpty())
					newProject.setFloorPlan(floorPlanList);
			}
			projectRepository.save(newProject);
			return ResponseEntity.status(HttpStatus.OK).body(
					new ProjectAndFloorPlanApiResponse(Messages.PROJECT_OK_REQUEST + " id " + newProject.getId()));
		} catch (ProjectException e) {
			logger.error("Unable to add Project with projectName {}.{}", projectName, e);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ProjectAndFloorPlanApiResponse(Messages.BAD_REQUEST));
	}

	private boolean isProjectIdValid(Long id) {
		return projectRepository.findById(id).isPresent();
	}

	private boolean isFloorPlanIdValid(Long id) {
		return floorPlanRepository.findById(id).isPresent();
	}

	@Override
	public ResponseEntity<ProjectAndFloorPlanApiResponse> moveFloorPlanToOtherProject(Long projectId, Long floorId) {
		try {
			if (isProjectIdValid(projectId) && isFloorPlanIdValid(floorId)) {
				Project project = projectRepository.findById(projectId).get();
				List<FloorPlan> floorPlanList = project.getFloorPlan();
				floorPlanList.add(floorPlanRepository.findById(floorId).get());
				project.setFloorPlan(floorPlanList);
				return ResponseEntity.status(HttpStatus.OK).body(new ProjectAndFloorPlanApiResponse(
						Messages.FLOORPLAN_MOVED_SUCCESSFULLY + "projectId " + projectId));
			}

		} catch (ProjectException e) {
			logger.error("Unable to move floorPlan to project id {}.{}", projectId, e);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ProjectAndFloorPlanApiResponse(Messages.BAD_REQUEST));
	}

}
