package com.floorplansapi.constants;

/*Constants class to manage all the constant variables
used in this application */
public class Messages {
	public static final String BAD_REQUEST = "Bad Request.";
	public static final String OK_REQUEST = "Successful.";

	public static final String PROJECT_OK_REQUEST = "Project added successfully.";
	public static final String FLOORPLAN_OK_REQUEST = "Floorplan added successfully.";
	public static final String FLOORPLAN_MOVED_SUCCESSFULLY = "Floorplan moved successfully.";
	public static final String NAME_UPDATED_SUCCESSFULLY = "Name updated successfully";


	private Messages() {
		throw new IllegalStateException("Utility class");
	}
}
