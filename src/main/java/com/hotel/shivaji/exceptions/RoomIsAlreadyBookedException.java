package com.hotel.shivaji.exceptions;

public class RoomIsAlreadyBookedException extends RuntimeException{

	    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private String message;

	    public RoomIsAlreadyBookedException() {}

	    public RoomIsAlreadyBookedException(String msg) {
	        super(msg);
	        this.message = msg;
	    }
	
}
