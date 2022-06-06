package com.floorplansapi.exceptions;

public class FloorException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FloorException(String message) {
        super(message);
    }
	
	public FloorException(Exception e) {
        super(e);
    }
	
	public FloorException(String message, Exception e) {
        super(message,e);
    }
}