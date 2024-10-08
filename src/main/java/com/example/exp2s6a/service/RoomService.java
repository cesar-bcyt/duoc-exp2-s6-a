package com.example.exp2s6a.service;

import java.util.List;
import java.util.Optional;

import com.example.exp2s6a.model.Room;

public interface RoomService {
    List<Room> getAllRooms();
    Optional<Room> getRoomById(Long id);
    Room saveRoom(Room room);
    void deleteRoom(Long id);
    List<Room> getRoomsByHotelId(Long hotelId);
}