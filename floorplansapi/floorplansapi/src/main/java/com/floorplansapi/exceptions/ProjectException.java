package com.floorplansapi.exceptions;

public class ProjectException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ProjectException(String message) {
        super(message);
    }
	
	public ProjectException(Exception e) {
        super(e);
    }
	
	public ProjectException(String message, Exception e) {
        super(message,e);
    }
}