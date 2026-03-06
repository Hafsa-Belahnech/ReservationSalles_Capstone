package com.example.repository;

import com.example.model.Salle;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface SalleRepository {

    // Trouver les salles disponibles par créneau
    List<Salle> findAvailableRooms(LocalDateTime start, LocalDateTime end);

    // La recherche multi-critères
    List<Salle> findByCriteria(Map<String, Object> criteria);

    // La pagination
    List<Salle> findAllPaginated(int page, int size);

    // Compter le nombre total de salles, c'est pour la pagination
    long count();

    // Méthodes CRUD
    Salle findById(Long id);
    List<Salle> findAll();
    void save(Salle salle);
    void update(Salle salle);
    void delete(Salle salle);
}


