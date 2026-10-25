package com.hotelparaiso.sistema_hotelero.repository;

import com.hotelparaiso.sistema_hotelero.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Integer> {
    List<CategoriaEntity> findByActivoTrue();
    boolean existsByNombreIgnoreCaseAndIdNotAndActivoTrue(String nombre, int id);
}