package com.example.repository;

import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.StatutReservation;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationRepositoryImpl implements ReservationRepository {

    private final EntityManager em;

    public ReservationRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Reservation create(Reservation reservation) {
        em.getTransaction().begin();
        em.persist(reservation);
        em.getTransaction().commit();
        return reservation;
    }

    @Override
    public Reservation findById(Long id) {
        return em.find(Reservation.class, id);
    }

    /**
     * Trouve toutes les réservations pour une salle donnée qui entrent en conflit
     * avec un créneau horaire spécifié. Un conflit existe si les périodes se chevauchent.
     */
    @Override
    public List<Reservation> findBySalleAndDateRange(Salle salle, LocalDateTime debut, LocalDateTime fin) {
        String jpql = "SELECT r FROM Reservation r WHERE r.salle = :salle " +
                "AND r.statut != :statutAnnulee " +
                "AND ((r.dateDebut < :fin AND r.dateFin > :debut))";

        return em.createQuery(jpql, Reservation.class)
                .setParameter("salle", salle)
                .setParameter("statutAnnulee", StatutReservation.ANNULEE)
                .setParameter("debut", debut)
                .setParameter("fin", fin)
                .getResultList();
    }

    @Override
    public List<Reservation> findByStatut(StatutReservation statut) {
        return em.createQuery("SELECT r FROM Reservation r WHERE r.statut = :statut", Reservation.class)
                .setParameter("statut", statut)
                .getResultList();
    }

    @Override
    public Reservation update(Reservation reservation) {
        em.getTransaction().begin();
        // merge() met à jour l'entité et gère le champ @Version pour l'optimistic locking
        Reservation updatedReservation = em.merge(reservation);
        em.getTransaction().commit();
        return updatedReservation;
    }

    @Override
    public void delete(Long id) {
        Reservation reservation = findById(id);
        if (reservation != null) {
            em.getTransaction().begin();
            em.remove(reservation);
            em.getTransaction().commit();
        }
    }
}
