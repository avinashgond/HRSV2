package com.hotel.shivaji.entities;

import com.hotel.shivaji.constants.RoomStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rooms {
	
	/**
	 * roomNumber -- This will contains room numbers
	 */
	@Id
	private String roomNumber;
	
	/**
	 * availableRooms -- This will contains number of available rooms 
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private RoomStatus status;
	
	/**
	 * floors -- This will contains floor data
	 */
	private String floors;

}
