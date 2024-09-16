package com.example.exp2s6a.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.exp2s6a.model.Reservation;

public interface ReservationService {
    List<Reservation> getAllReservations();
    Optional<Reservation> getReservationById(Long id);
    Reservation saveReservation(Reservation reservation);
    void deleteReservation(Long id);
    List<Reservation> getReservationsByRoomId(Long roomId);
    List<Reservation> getReservationsBetweenDates(LocalDate startDate, LocalDate endDate);
    List<LocalDate> getAvailableDaysBetweenDates(Long roomId, LocalDate startDate, LocalDate endDate);
}