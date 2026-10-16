package com.hotelparaiso.sistema_hotelero.repository;
import com.hotelparaiso.sistema_hotelero.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
    List<Pago> findAllByOrderByIdDesc();
    List<Pago> findByReserva_Id(int reservaId);
}