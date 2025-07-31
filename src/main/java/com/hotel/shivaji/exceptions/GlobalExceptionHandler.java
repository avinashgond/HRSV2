package com.hotel.shivaji.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RoomIsAlreadyBookedException.class)
	public ResponseEntity<String> handleRoomIsAlreadyBookedException(RoomIsAlreadyBookedException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	

    @ExceptionHandler(MoreThanFiveRoomsNotAllowed.class)
    public ResponseEntity<String> handleMoreThanFiveRoomsNotAllowed(MoreThanFiveRoomsNotAllowed ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}





