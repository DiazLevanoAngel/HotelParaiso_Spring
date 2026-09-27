package com.hotelparaiso.sistema_hotelero.service;

import com.hotelparaiso.sistema_hotelero.model.Reserva;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ReservaService {

    private final List<Reserva> reservas = new ArrayList<>();
    private int contadorId;

    public List<Reserva> listar() {
        return reservas.stream()
                .sorted(Comparator.comparingInt(Reserva::getId).reversed())
                .toList();
    }

    public Reserva buscarPorId(int id) {
        return reservas.stream()
                .filter(reserva -> reserva.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public long contarActivas() {
        return reservas.stream()
                .filter(reserva -> !"Cancelada".equals(reserva.getEstado()))
                .count();
    }

    public Reserva guardar(Reserva reserva) {
        validar(reserva);

        if (reserva.getId() == 0) {
            reserva.setId(++contadorId);
            reservas.add(reserva);
            return reserva;
        }

        for (int i = 0; i < reservas.size(); i++) {
            if (reservas.get(i).getId() == reserva.getId()) {
                reservas.set(i, reserva);
                return reserva;
            }
        }

        throw new IllegalArgumentException("No se encontró la reserva que intentas editar.");
    }

    public boolean eliminar(int id) {
        return reservas.removeIf(reserva -> reserva.getId() == id);
    }

    public void cancelar(int id) {
        Reserva reserva = buscarPorId(id);
        if (reserva == null) {
            throw new IllegalArgumentException("No se encontró la reserva que intentas cancelar.");
        }
        reserva.setEstado("Cancelada");
    }

    private void validar(Reserva reserva) {
        if (reserva.getCliente() == null || reserva.getCliente().isBlank()) {
            throw new IllegalArgumentException("Ingresa el nombre del cliente.");
        }
        if (reserva.getHabitacion() == null || reserva.getHabitacion().isBlank()) {
            throw new IllegalArgumentException("Ingresa la habitación.");
        }
        if (reserva.getCheckIn() == null || reserva.getCheckOut() == null
                || !reserva.getCheckOut().isAfter(reserva.getCheckIn())) {
            throw new IllegalArgumentException("La salida debe ser posterior a la fecha de entrada.");
        }
        if (reserva.getHuespedes() < 1) {
            throw new IllegalArgumentException("La reserva debe tener al menos un huésped.");
        }
        if (reserva.getMonto() == null || reserva.getMonto().signum() < 0) {
            throw new IllegalArgumentException("El monto debe ser igual o mayor que cero.");
        }
        if (!List.of("Pendiente", "Confirmada", "Cancelada").contains(reserva.getEstado())) {
            throw new IllegalArgumentException("Selecciona un estado válido para la reserva.");
        }
        reserva.setCliente(reserva.getCliente().trim());
        reserva.setHabitacion(reserva.getHabitacion().trim());
    }
}
