package com.example.exp2s6a.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.exp2s6a.model.Reservation;
import com.example.exp2s6a.repository.ReservationRepository;
import com.example.exp2s6a.service.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        List<Reservation> overlappingReservations = reservationRepository
            .findAll()
            .stream()
            .filter(x -> !Objects.equals(x.getId(), reservation.getId())) // Excluir la reserva actual
            .filter(x -> x.getRoom().getId().equals(reservation.getRoom().getId())) // Obtener reservas de la misma habitación
            .filter(x -> overlappingRange(x.getCheckInDate(), x.getCheckOutDate(), reservation.getCheckInDate(), reservation.getCheckOutDate())) // Incluir reservas que se superponen
            .collect(Collectors.toList());

        if (!overlappingReservations.isEmpty()) {
            throw new IllegalStateException("Ya existe una reserva para esta habitación y rango de fechas.");
        }

        return reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public List<Reservation> getReservationsByRoomId(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }

    @Override
    public List<Reservation> getReservationsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return reservationRepository.findByCheckInDateBetweenOrCheckOutDateBetween(startDate, endDate, startDate, endDate);
    }

    @Override
    public List<LocalDate> getAvailableDaysBetweenDates(Long roomId, LocalDate startDate, LocalDate endDate) {
        List<Reservation> reservations = reservationRepository.findByRoomIdAndDateRange(roomId, startDate, endDate);
        List<LocalDate> allDates = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
        List<LocalDate> reservedDates = new ArrayList<>();

        for (Reservation reservation : reservations) {
            LocalDate reservationStart = reservation.getCheckInDate();
            LocalDate reservationEnd = reservation.getCheckOutDate();
            reservedDates.addAll(reservationStart.datesUntil(reservationEnd).collect(Collectors.toList()));
        }

        return allDates.stream()
                .filter(date -> !reservedDates.contains(date))
                .collect(Collectors.toList());
    }

    private boolean overlappingRange(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}