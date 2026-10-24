package com.hotelparaiso.sistema_hotelero.repository;

import com.hotelparaiso.sistema_hotelero.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Integer> {
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, int id);
}