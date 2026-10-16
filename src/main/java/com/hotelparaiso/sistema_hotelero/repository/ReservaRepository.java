package com.hotelparaiso.sistema_hotelero.repository;
import com.hotelparaiso.sistema_hotelero.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findAllByOrderByIdDesc();
    long countByClienteIgnoreCase(String cliente);
    boolean existsByClienteIgnoreCase(String cliente);
    boolean existsByHabitacionIgnoreCase(String habitacion);
    List<Reserva> findByHabitacionIgnoreCase(String habitacion);
}
