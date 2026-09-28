// service/ReservaService.java
package com.hotelparaiso.sistema_hotelero.service;

import com.hotelparaiso.sistema_hotelero.model.Reserva;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservaService {

    private List<Reserva> reservas = new ArrayList<>();
    private int contadorId = 0;

    public List<Reserva> listar() {
        List<Reserva> resultado = new ArrayList<>();
        for (int i = reservas.size() - 1; i >= 0; i--) {
            resultado.add(reservas.get(i));
        }
        return resultado;
    }

    public Reserva buscarPorID(int id) {
        for (Reserva reserva : reservas) {
            if (reserva.getId() == id) {
                return reserva;
            }
        }
        return null;
    }

    public int contarActivas() {
        int total = 0;
        for (Reserva reserva : reservas) {
            if (!"Cancelada".equals(reserva.getEstado())) {
                total++;
            }
        }
        return total;
    }

    public Reserva guardarReserva(Reserva reserva) {
        if (reserva.getCheckIn() == null || reserva.getCheckOut() == null
                || !reserva.getCheckOut().isAfter(reserva.getCheckIn())) {
            throw new IllegalArgumentException("La salida debe ser posterior a la fecha de entrada.");
        }
        if (reserva.getId() == 0) {
            contadorId++;
            reserva.setId(contadorId);
            reservas.add(reserva);
        } else {
            for (int i = 0; i < reservas.size(); i++) {
                if (reservas.get(i).getId() == reserva.getId()) {
                    reservas.set(i, reserva);
                }
            }
        }
        return reserva;
    }

    public boolean eliminar(int id) {
        for (int i = reservas.size() - 1; i >= 0; i--) {
            if (reservas.get(i).getId() == id) {
                reservas.remove(i);
                return true;
            }
        }
        return false;
    }
}