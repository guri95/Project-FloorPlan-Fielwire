package com.floorplansapi.response;

/*
 * ProjectResponse class to store responsive message 
 * so that can be use for API response if API call
 * is successfully executed
*/
public class ProjectAndFloorPlanApiResponse {
	private String message;

	public ProjectAndFloorPlanApiResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
