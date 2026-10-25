package com.hotelparaiso.sistema_hotelero.repository;

import com.hotelparaiso.sistema_hotelero.entity.HabitacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitacionRepository extends JpaRepository<HabitacionEntity, Integer> {
    List<HabitacionEntity> findByActivoTrue();
    List<HabitacionEntity> findByEstadoIgnoreCaseAndActivoTrue(String estado);
    Optional<HabitacionEntity> findByNumeroIgnoreCaseAndActivoTrue(String numero);
    boolean existsByNumeroIgnoreCaseAndIdNotAndActivoTrue(String numero, int id);
    boolean existsByCategoria_IdAndActivoTrue(int categoriaId);
}