package com.hotel.shivaji.exceptions;

public class MoreThanFiveRoomsNotAllowed extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MoreThanFiveRoomsNotAllowed(String message) {
        super(message);
    }
}

