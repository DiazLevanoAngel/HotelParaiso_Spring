package com.hotelparaiso.sistema_hotelero.repository;

import com.hotelparaiso.sistema_hotelero.entity.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Integer> {
    List<ReservaEntity> findAllByOrderByIdDesc();
    long countByClienteIgnoreCase(String cliente);
    long countByEstadoNot(String estado);
    boolean existsByClienteIgnoreCase(String cliente);
    boolean existsByHabitacionIgnoreCase(String habitacion);
    List<ReservaEntity> findByHabitacionIgnoreCase(String habitacion);
}