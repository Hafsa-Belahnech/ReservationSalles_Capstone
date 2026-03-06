package com.example.service;

import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.Utilisateur;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    Reservation createReservation(Salle salle, Utilisateur utilisateur, LocalDateTime debut, LocalDateTime fin, String motif);
    Reservation getReservationById(Long id);
    List<Reservation> getReservationsForRoom(Salle salle, LocalDateTime debut, LocalDateTime fin);
    boolean cancelReservation(Long reservationId);
    Reservation updateReservation(Long reservationId, LocalDateTime newDebut, LocalDateTime newFin, String newMotif);
}
