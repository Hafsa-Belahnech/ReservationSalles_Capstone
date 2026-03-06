package com.example.service;

import com.example.model.Salle;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface SalleService {

    // Trouver les salles disponibles par créneau
    List<Salle> findAvailableRooms(LocalDateTime start, LocalDateTime end);

    // La recherche multi-critères
    List<Salle> searchRooms(Map<String, Object> criteria);

    // La pagination
    List<Salle> getPaginatedRooms(int page, int size);

    // Obtenir le nombre total de pages
    int getTotalPages(int size);

    // Autres méthodes de service
    Salle getRoomById(Long id);
    List<Salle> getAllRooms();
    void saveRoom(Salle salle);
    void updateRoom(Salle salle);
    void deleteRoom(Salle salle);

    long countRooms();
}
