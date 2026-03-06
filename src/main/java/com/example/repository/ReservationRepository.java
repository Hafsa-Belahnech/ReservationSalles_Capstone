package com.example.repository;

import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.StatutReservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository {
    Reservation create(Reservation reservation);
    Reservation findById(Long id);
    List<Reservation> findBySalleAndDateRange(Salle salle, LocalDateTime debut, LocalDateTime fin);
    List<Reservation> findByStatut(StatutReservation statut);
    Reservation update(Reservation reservation);
    void delete(Long id);
}