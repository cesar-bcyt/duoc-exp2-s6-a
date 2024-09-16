package com.example.exp2s6a.service;

import java.util.List;
import java.util.Optional;

import com.example.exp2s6a.model.Hotel;

public interface HotelService {
    List<Hotel> getAllHotels();
    Optional<Hotel> getHotelById(Long id);
    Hotel saveHotel(Hotel hotel);
    void deleteHotel(Long id);
}