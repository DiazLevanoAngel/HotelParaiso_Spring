package com.hotelparaiso.sistema_hotelero.repository;

import com.hotelparaiso.sistema_hotelero.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Integer> {
    List<ClienteEntity> findByActivoTrue();
}