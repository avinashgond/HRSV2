package com.hotel.shivaji.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hotel.shivaji.entities.Rooms;

/**
 * RoomService -- Interface to have all of the room related methods.
 */
@Service
public interface RoomService {

	/**
	 * addRoom -- Use to add rooms in database
	 * @return List<Rooms> -- Will return list of rooms
	 */
	public List<Rooms> addRoom();

	/**
	 * getAllRooms -- Use to get all rooms
	 * @return List<Rooms> -- Will return list of available rooms
	 */
	public List<Rooms> getAllRooms();

	/**
	 * getAvailableRoomsCount -- This will help to count available rooms
	 * @return Long -- will return count of available rooms in Long type
	 */
	public Long getAvailableRoomsCount();

	/**
	 * bookRoom -- This will helps to book the rooms
	 * @return List<Rooms> -- will return list of booked rooms
	 */
	public List<Rooms> bookRoom(Integer totalRoom);

	/**
	 * generateRandomOccupancy -- This will helps to book rooms randomly
	 * @return List<Rooms> -- will return list of randomly booked rooms
	 */
	public List<Rooms> generateRandomOccupancy();

	/**
	 * resetAllBookings -- This will helps to reset all booked rooms
	 * @return void -- will not return anything
	 */
	public void resetAllBookings();
}
