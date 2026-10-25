package com.hotelparaiso.sistema_hotelero.repository;

import com.hotelparaiso.sistema_hotelero.entity.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoRepository extends JpaRepository<PagoEntity, Integer> {
    List<PagoEntity> findAllByActivoTrueOrderByIdDesc();
    List<PagoEntity> findByActivoTrue();
    List<PagoEntity> findByReserva_IdAndActivoTrue(int reservaId);
    List<PagoEntity> findByReserva_Id(int reservaId);
}