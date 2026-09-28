package com.hotelparaiso.sistema_hotelero.service;

import com.hotelparaiso.sistema_hotelero.model.Reserva;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservaServiceTest {

    private final ReservaService reservaService = new ReservaService();

    @Test
    void createsUpdatesAndDeletesReservation() {
        Reserva reserva = crearReserva();
        reservaService.guardar(reserva);

        assertEquals(1, reservaService.listar().size());
        assertEquals(1, reservaService.contarActivas());

        reserva.setCliente("Ana Pérez");
        reserva.setEstado("Cancelada");
        reservaService.guardar(reserva);

        assertEquals("Ana Pérez", reservaService.buscarPorId(reserva.getId()).getCliente());
        assertEquals(0, reservaService.contarActivas());
        assertTrue(reservaService.eliminar(reserva.getId()));
        assertTrue(reservaService.listar().isEmpty());
        assertFalse(reservaService.eliminar(reserva.getId()));
    }

    @Test
    void rejectsCheckoutBeforeCheckin() {
        Reserva reserva = crearReserva();
        reserva.setCheckOut(reserva.getCheckIn());

        assertThrows(IllegalArgumentException.class, () -> reservaService.guardar(reserva));
        assertTrue(reservaService.listar().isEmpty());
    }

    private Reserva crearReserva() {
        Reserva reserva = new Reserva();
        reserva.setCliente("Ana Pérez");
        reserva.setHabitacion("101 — Individual");
        reserva.setCheckIn(LocalDate.of(2026, 10, 1));
        reserva.setCheckOut(LocalDate.of(2026, 10, 3));
        reserva.setHuespedes(2);
        reserva.setMonto(new BigDecimal("250.00"));
        return reserva;
    }
}
