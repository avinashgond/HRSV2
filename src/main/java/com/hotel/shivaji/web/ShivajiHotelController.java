package com.hotel.shivaji.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.shivaji.entities.Rooms;
import com.hotel.shivaji.services.RoomService;

@RestController
@RequestMapping("/shivaji-hotel")
public class ShivajiHotelController {
	
	@Autowired
	private RoomService roomService;

	@GetMapping("/available-rooms")
	public ResponseEntity<List<Rooms>> getAvailableRooms(){
		return new ResponseEntity<>(roomService.getAllRooms(), HttpStatus.OK);
	}
	
	@GetMapping("/available-rooms-count")
	public ResponseEntity<String> getAvailableRoomsCount(){
		Long availableRoomsCount = roomService.getAvailableRoomsCount();
		return new ResponseEntity<>("Available Rooms : "+availableRoomsCount, HttpStatus.OK);
	}
	
	@PostMapping("/add-room")
	public ResponseEntity<List<Rooms>> addRoom(){
		return new ResponseEntity<List<Rooms>>(roomService.addRoom(), HttpStatus.CREATED);
	}
	
	@PostMapping("/book-room/{totalRoom}")
	public ResponseEntity<List<Rooms>> bookRoom(@PathVariable Integer totalRoom){
		return new ResponseEntity<List<Rooms>>(roomService.bookRoom(totalRoom), HttpStatus.CREATED);
	}
	
	@PostMapping("/rooms/random-occupancy")
	public ResponseEntity<List<Rooms>> randomOccupancy() {
	    return new ResponseEntity<>(roomService.generateRandomOccupancy(),HttpStatus.CREATED);
	}

	@PostMapping("/rooms/reset")
	public ResponseEntity<String> resetBookings() {
	    roomService.resetAllBookings();
	    return ResponseEntity.ok("All bookings have been reset.");
	}

}
