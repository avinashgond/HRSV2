package com.hotel.shivaji.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotel.shivaji.constants.RoomStatus;
import com.hotel.shivaji.entities.Rooms;

@Repository
public interface RoomRepository extends JpaRepository<Rooms, String>{

	/**
	 * findByStatus -- This will fetch the room data by status
	 * @param status -- contains status
	 * @return List<Rooms> -- will return List of room
	 */
	List<Rooms> findByStatus(RoomStatus status);
}
