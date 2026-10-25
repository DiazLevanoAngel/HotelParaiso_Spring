package com.hotelparaiso.sistema_hotelero.repository;

import com.hotelparaiso.sistema_hotelero.entity.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Integer> {
    List<ReservaEntity> findAllByActivoTrueOrderByIdDesc();
    long countByClienteIgnoreCaseAndEstadoReservaNotAndActivoTrue(String cliente, String estadoReserva);
    long countByEstadoReservaNotAndActivoTrue(String estadoReserva);
    boolean existsByClienteIgnoreCaseAndEstadoReservaNotAndActivoTrue(String cliente, String estadoReserva);
    boolean existsByHabitacionIgnoreCaseAndEstadoReservaNotAndActivoTrue(String habitacion, String estadoReserva);
    List<ReservaEntity> findByHabitacionIgnoreCaseAndActivoTrue(String habitacion);
    
}