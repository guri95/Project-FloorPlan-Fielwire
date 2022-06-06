package com.floorplansapi.sevice;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
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
import com.floorplansapi.exceptions.FloorException;
import com.floorplansapi.imageprocessor.ImageController;
import com.floorplansapi.repository.FloorPlanRepository;
import com.floorplansapi.repository.ProjectRepository;
import com.floorplansapi.response.ProjectAndFloorPlanApiResponse;

@Service
public class FloorPlanServiceImpl implements FloorPlanService {
	@Autowired
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

	@Autowired
	private FloorPlanRepository floorPlanRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ImageController imageController;
	
	@Autowired
	private CommonServices commonServices;

	@Override
	public ResponseEntity<ProjectAndFloorPlanApiResponse> deleteFloorPlanByid(Long id) {
		try {
			if (commonServices.isFloorPlanIdValid(id)) {
				floorPlanRepository.deleteById(id);
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ProjectAndFloorPlanApiResponse("Project removed Successfully: id " + id));
			}
		} catch (FloorException e) {
			logger.error("Unable to delete floorPlan id {}.{}", id, e);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ProjectAndFloorPlanApiResponse(Messages.BAD_REQUEST));
	}

	@Override
	public List<FloorPlan> getFloorPlanListByProjectId(Long id) {

		try {
			if (commonServices.isProjectIdValid(id)) {
				return projectRepository.findById(id).get().getFloorPlan();
			}
		} catch (FloorException e) {
			logger.error("Unable to get floorPlan list by projec id {}.{}", id, e);
		}

		return Collections.emptyList();
	}

	@Override
	public FloorPlan getFloorPlanById(Long id) {
		try {
			if (commonServices.isFloorPlanIdValid(id)) {
				return floorPlanRepository.findById(id).get();
			}
		} catch (FloorException e) {
			logger.error("Unable to get floorPlan by floorPlan id {}.{}", id, e);
		}
		return null;
	}

	@Override
	public ResponseEntity<ProjectAndFloorPlanApiResponse> addFloorPlan(MultipartFile file, String floorPlanName)
			throws IOException {
		try {
			FloorPlan floorplan = new FloorPlan();
			if (file != null && !file.isEmpty()) {
				byte[] fileBytes = file.getBytes();
				floorplan.setOriginalImage(fileBytes);
				floorplan.setThum(imageController.compressMultiparFileImage(file, Constants.THUMBNAIL));
				floorplan.setLarge(imageController.compressMultiparFileImage(file, Constants.LARGEIMAGE));
			}
			if(floorPlanName == null) {
				floorPlanName = "";
			}
			floorplan.setName(floorPlanName);
			floorPlanRepository.save(floorplan);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ProjectAndFloorPlanApiResponse("floorPlan added successfully."));

		} catch (FloorException e) {
			logger.error("Unable creat floorPlan by floorPlan by FloorPlan name {}.{}", floorPlanName, e);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ProjectAndFloorPlanApiResponse(Messages.BAD_REQUEST));
	}

	@Override
	public ResponseEntity<ProjectAndFloorPlanApiResponse> updateFloorPlan(MultipartFile file, String floorPlanName,
			Long floorPlanId) throws IOException {
		try {
			FloorPlan floorplan = new FloorPlan();

			if (commonServices.isFloorPlanIdValid(floorPlanId)) {
				floorplan = floorPlanRepository.findById(floorPlanId).get();
				if (file != null && !file.isEmpty()) {
					byte[] fileBytes = file.getBytes();
					floorplan.setOriginalImage(fileBytes);
					floorplan.setThum(imageController.compressMultiparFileImage(file, Constants.THUMBNAIL));
					floorplan.setLarge(imageController.compressMultiparFileImage(file, Constants.LARGEIMAGE));
				}

			} else {
				floorplan.setId(floorPlanId);
			}

			if (floorPlanName == null) {
				floorPlanName = "";
			}
			floorplan.setName(floorPlanName);
			floorPlanRepository.save(floorplan);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ProjectAndFloorPlanApiResponse("floorPlan updated successfully."));

		} catch (FloorException e) {
			logger.error("Unable creat floorPlan by floorPlan by FloorPlan name {}.{}", floorPlanName, e);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ProjectAndFloorPlanApiResponse(Messages.BAD_REQUEST));
	}

	@Override
	public ResponseEntity<ProjectAndFloorPlanApiResponse> moveFloorPlanByProjectId(Long projectId, Long floorPlanId) {
		try {
			
			if(commonServices.isFloorPlanIdValid(floorPlanId) && commonServices.isProjectIdValid(projectId)) {
				Project project = projectRepository.findById(projectId).get();
				FloorPlan floorPlan = floorPlanRepository.findById(floorPlanId).get();
				
				List<FloorPlan> floorPlanList = project.getFloorPlan();
				floorPlanList.add(floorPlan);
				project.setFloorPlan(floorPlanList);
				projectRepository.save(project);
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ProjectAndFloorPlanApiResponse("floorPlan updated successfully."));
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ProjectAndFloorPlanApiResponse("invalid id's"));
			}

		} catch (FloorException e) {
			logger.error("Unable update floorPlan by projectId {}.{}", projectId, e);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ProjectAndFloorPlanApiResponse(Messages.BAD_REQUEST));	}

}
