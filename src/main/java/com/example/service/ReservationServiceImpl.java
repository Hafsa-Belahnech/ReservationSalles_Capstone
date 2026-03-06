package com.example.service;

import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.StatutReservation;
import com.example.model.Utilisateur;
import com.example.repository.ReservationRepository;

import javax.persistence.OptimisticLockException;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Crée une nouvelle réservation après avoir vérifié que la salle est disponible.
     */
    @Override
    public Reservation createReservation(Salle salle, Utilisateur utilisateur, LocalDateTime debut, LocalDateTime fin, String motif) {
        // Vérifier les conflits avant de créer
        List<Reservation> conflicts = reservationRepository.findBySalleAndDateRange(salle, debut, fin);
        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("La salle n'est pas disponible pour ce créneau horaire.");
        }

        Reservation reservation = new Reservation();
        reservation.setSalle(salle);
        reservation.setUtilisateur(utilisateur);
        reservation.setDateDebut(debut);
        reservation.setDateFin(fin);
        reservation.setMotif(motif);
        reservation.setStatut(StatutReservation.CONFIRMEE); // Confirmation automatique pour simplifier

        return reservationRepository.create(reservation);
    }

    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public List<Reservation> getReservationsForRoom(Salle salle, LocalDateTime debut, LocalDateTime fin) {
        return reservationRepository.findBySalleAndDateRange(salle, debut, fin);
    }

    /**
     * Annule une réservation en changeant son statut.
     */
    @Override
    public boolean cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId);
        if (reservation != null && reservation.getStatut() != StatutReservation.ANNULEE) {
            reservation.setStatut(StatutReservation.ANNULEE);
            reservationRepository.update(reservation);
            return true;
        }
        return false;
    }

    /**
     * Met à jour une réservation existante. Gère les conflits de dates et l'optimistic locking.
     */
    @Override
    public Reservation updateReservation(Long reservationId, LocalDateTime newDebut, LocalDateTime newFin, String newMotif) {
        Reservation reservation = reservationRepository.findById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Réservation non trouvée");
        }

        // Vérifier les conflits pour le nouveau créneau, en excluant la réservation elle-même
        List<Reservation> conflicts = reservationRepository.findBySalleAndDateRange(reservation.getSalle(), newDebut, newFin);
        for (Reservation conflict : conflicts) {
            if (!conflict.getId().equals(reservationId)) {
                throw new IllegalStateException("Le nouveau créneau entre en conflit avec une autre réservation.");
            }
        }

        reservation.setDateDebut(newDebut);
        reservation.setDateFin(newFin);
        reservation.setMotif(newMotif);

        try {
            return reservationRepository.update(reservation);
        } catch (OptimisticLockException e) {
            throw new IllegalStateException("La réservation a été modifiée par un autre utilisateur. Veuillez réessayer.", e);
        }
    }
}
