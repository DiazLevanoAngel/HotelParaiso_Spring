package com.hotelparaiso.sistema_hotelero.repository;

import com.hotelparaiso.sistema_hotelero.entity.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Integer> {
    List<ReservaEntity> findAllByActivoTrueOrderByIdDesc();
    long countByClienteIgnoreCaseAndActivoTrue(String cliente);
    long countByEstadoNotAndActivoTrue(String estado);
    boolean existsByClienteIgnoreCaseAndActivoTrue(String cliente);
    boolean existsByHabitacionIgnoreCaseAndActivoTrue(String habitacion);
    List<ReservaEntity> findByHabitacionIgnoreCaseAndActivoTrue(String habitacion);
    
}