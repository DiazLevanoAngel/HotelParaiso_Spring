package com.hotelparaiso.sistema_hotelero.repository;

import com.hotelparaiso.sistema_hotelero.entity.HabitacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitacionRepository extends JpaRepository<HabitacionEntity, Integer> {
    boolean existsByNumeroIgnoreCaseAndIdNot(String numero, int id);
    boolean existsByCategoria_Id(int categoriaId);
    List<HabitacionEntity> findByEstadoIgnoreCase(String estado);
    Optional<HabitacionEntity> findByNumeroIgnoreCase(String numero);
}