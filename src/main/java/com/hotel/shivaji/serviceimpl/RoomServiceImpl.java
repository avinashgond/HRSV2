package com.hotel.shivaji.serviceimpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hotel.shivaji.constants.RoomStatus;
import com.hotel.shivaji.entities.Rooms;
import com.hotel.shivaji.exceptions.MoreThanFiveRoomsNotAllowed;
import com.hotel.shivaji.exceptions.RoomIsAlreadyBookedException;
import com.hotel.shivaji.repositories.RoomRepository;
import com.hotel.shivaji.services.RoomService;

import jakarta.transaction.Transactional;

@Repository
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Override
	@Transactional
	public List<Rooms> addRoom() {
		List<Rooms> roomsToSave = new ArrayList<>();

		for (int i = 1; i <= 10; i++) {
			int roomCount = (i == 10) ? 7 : 10;

			for (int j = 1; j <= roomCount; j++) {
				Rooms room = new Rooms();

				String roomNumber = (j == 10 && i <= 9) ? i + "" + j : i + "0" + j;
				room.setRoomNumber(roomNumber);
				room.setFloors("Floor " + i);
				room.setStatus(RoomStatus.AVAILABLE);
				roomsToSave.add(room);
			}
		}
		List<Rooms> allRooms = roomRepository.saveAll(roomsToSave); // Batch insert
		return allRooms;
	}

	@Override
	public List<Rooms> getAllRooms() {
		return roomRepository.findAll();

	}

	@Override
	public Long getAvailableRoomsCount() {
		List<Rooms> allRooms = roomRepository.findAll();
		return allRooms.stream().filter(x -> x.getStatus().equals(RoomStatus.AVAILABLE)).count();
	}

	private int extractFloorNumber(String floorStr) {
		return Integer.parseInt(floorStr.replaceAll("[^0-9]", ""));
	}

	private int getRoomPosition(String roomStr) {
		int num = Integer.parseInt(roomStr);
		return num >= 1001 ? num - 1000 : num % 100;
	}

	@Override
	public List<Rooms> generateRandomOccupancy() {

		List<Rooms> allRooms = roomRepository.findAll();
		Random random = new Random();

		for (Rooms room : allRooms) {
			RoomStatus status = random.nextBoolean() ? RoomStatus.BOOKED : RoomStatus.AVAILABLE;
			room.setStatus(status);
		}

		return roomRepository.saveAll(allRooms);

	}

	@Override
	public void resetAllBookings() {

		List<Rooms> allRooms = roomRepository.findAll();

		for (Rooms room : allRooms) {
			room.setStatus(RoomStatus.AVAILABLE);
		}

		roomRepository.saveAll(allRooms);

	}

	@Override
	public List<Rooms> bookRoom(Integer totalRoom) {
		if (totalRoom > 5) {
			throw new MoreThanFiveRoomsNotAllowed("Only 5 Rooms Allowed At A Time");
		}
		List<Rooms> availableRooms = roomRepository.findByStatus(RoomStatus.AVAILABLE);

		if (availableRooms.size() < totalRoom) {
			throw new RoomIsAlreadyBookedException("Not enough rooms available.");
		}

		// Step 1: Group available rooms by floor
		Map<String, List<Rooms>> roomsByFloor = availableRooms.stream()
				.collect(Collectors.groupingBy(Rooms::getFloors));

		// Step 2: Try booking from a single floor (prioritizing lowest floor + leftmost
		// rooms)
		for (String floor : roomsByFloor.keySet().stream().sorted(Comparator.comparingInt(this::extractFloorNumber))
				.collect(Collectors.toList())) {
			List<Rooms> floorRooms = roomsByFloor.get(floor);

			// Sort rooms by left to right (e.g., 101, 102, ..., 110)
			floorRooms.sort(Comparator.comparingInt(r -> getRoomPosition(r.getRoomNumber())));

			if (floorRooms.size() >= totalRoom) {
				List<Rooms> roomsToBook = floorRooms.subList(0, totalRoom);
				roomsToBook.forEach(r -> r.setStatus(RoomStatus.BOOKED));
				roomRepository.saveAll(roomsToBook);
				return roomsToBook;
			}
		}

		// Step 3: If no single floor has enough rooms, fallback to optimal cross-floor
		// logic
		List<Rooms> bestCombination = null;
		int minTravelTime = Integer.MAX_VALUE;

		List<List<Rooms>> combinations = generateCombinations(availableRooms, totalRoom);
		for (List<Rooms> combo : combinations) {
			int travelTime = calculateTravelTime(combo);
			if (travelTime < minTravelTime) {
				minTravelTime = travelTime;
				bestCombination = combo;
			}
		}

		if (bestCombination != null) {
			bestCombination.forEach(r -> r.setStatus(RoomStatus.BOOKED));
			roomRepository.saveAll(bestCombination);
			return bestCombination;
		}

		// No valid combination found
		throw new RoomIsAlreadyBookedException("Not enough optimal rooms available.");
	}

	private void generateCombinationsHelper(List<Rooms> rooms, int k, int start, List<Rooms> current,
			List<List<Rooms>> result) {
		if (current.size() == k) {
			result.add(new ArrayList<>(current));
			return;
		}
		for (int i = start; i < rooms.size(); i++) {
			current.add(rooms.get(i));
			generateCombinationsHelper(rooms, k, i + 1, current, result);
			current.remove(current.size() - 1);
		}
	}

	private int calculateTravelTime(List<Rooms> rooms) {
		if (rooms.size() <= 1)
			return 0;

		// Sort by floor and then by room number (as integer)
		rooms.sort(Comparator.comparing((Rooms r) -> extractFloorNumber(r.getFloors()))
				.thenComparing(r -> getRoomPosition(r.getRoomNumber())));

		int totalTravelTime = 0;

		for (int i = 0; i < rooms.size() - 1; i++) {
			Rooms room1 = rooms.get(i);
			Rooms room2 = rooms.get(i + 1);

			int floor1 = extractFloorNumber(room1.getFloors());
			int floor2 = extractFloorNumber(room2.getFloors());

			int roomPos1 = getRoomPosition(room1.getRoomNumber());
			int roomPos2 = getRoomPosition(room2.getRoomNumber());

			if (floor1 == floor2) {
				// Horizontal travel: 1 min per room gap
				totalTravelTime += Math.abs(roomPos1 - roomPos2);
			} else {
				// Vertical travel: 2 min per floor gap
				totalTravelTime += Math.abs(floor1 - floor2) * 2;
			}
		}
		return totalTravelTime;
	}

	private List<List<Rooms>> generateCombinations(List<Rooms> rooms, int k) {
		List<List<Rooms>> result = new ArrayList<>();
		generateCombinationsHelper(rooms, k, 0, new ArrayList<>(), result);
		return result;
	}
}
