package com.hotelparaiso.sistema_hotelero.service;

import com.hotelparaiso.sistema_hotelero.model.Reserva;
import com.hotelparaiso.sistema_hotelero.model.Habitacion;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class ReservaService {

    private List<Reserva> reservas = new ArrayList<>();
    private int contadorId = 0;
    private final HabitacionService habitacionService;
    private final CategoriaService categoriaService;

    public ReservaService(HabitacionService habitacionService, CategoriaService categoriaService) {
        this.habitacionService = habitacionService;
        this.categoriaService = categoriaService;
    }

    public List<Reserva> listar() {
        List<Reserva> resultado = new ArrayList<>();
        for (int i = reservas.size() - 1; i >= 0; i--) {
            resultado.add(reservas.get(i));
        }
        return resultado;
    }

    public List<Reserva> listarUltimas(int cantidad) {
        List<Reserva> resultado = listar();
        return resultado.size() > cantidad ? resultado.subList(0, cantidad) : resultado;
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

    public int contarPorCliente(String cliente) {
        int total = 0;
        for (Reserva reserva : reservas) {
            if (cliente.equalsIgnoreCase(reserva.getCliente())) {
                total++;
            }
        }
        return total;
    }

    public boolean tieneReservasCliente(String cliente) {
        return contarPorCliente(cliente) > 0;
    }

    public boolean tieneReservasHabitacion(String habitacion) {
        for (Reserva reserva : reservas) {
            if (habitacion.equalsIgnoreCase(reserva.getHabitacion())) {
                return true;
            }
        }
        return false;
    }

    public boolean habitacionDisponible(String habitacion, LocalDate entrada,
                                        LocalDate salida, int idExcluir) {
        for (Reserva reserva : reservas) {
            boolean mismaHabitacion = habitacion.equalsIgnoreCase(reserva.getHabitacion());
            boolean activa = !"Cancelada".equals(reserva.getEstado());
            boolean seCruzan = entrada.isBefore(reserva.getCheckOut())
                    && salida.isAfter(reserva.getCheckIn());

            if (reserva.getId() != idExcluir && mismaHabitacion && activa && seCruzan) {
                return false;
            }
        }
        return true;
    }

    public boolean registrarReserva(Reserva reserva) {
        Habitacion habitacion = habitacionService.buscarPorNumero(reserva.getHabitacion());
        if (habitacion == null || reserva.getCheckIn() == null || reserva.getCheckOut() == null
                || !reserva.getCheckOut().isAfter(reserva.getCheckIn())) {
            return false;
        }

        var categoria = categoriaService.buscarPorID(habitacion.getCategoriaId());
        if (categoria == null || !habitacionDisponible(reserva.getHabitacion(), reserva.getCheckIn(),
                reserva.getCheckOut(), reserva.getId())) {
            return false;
        }

        reserva.setHuespedes(categoria.getCapacidad());
        long noches = ChronoUnit.DAYS.between(reserva.getCheckIn(), reserva.getCheckOut());
        reserva.setMonto(categoria.getPrecio() * noches);
        if (contarPorCliente(reserva.getCliente()) >= 4) {
            reserva.setMonto(reserva.getMonto() * 0.85);
        }
        guardarReserva(reserva);
        return true;
    }

    public boolean ampliarReserva(int id, LocalDate nuevaFechaSalida) {
        Reserva reserva = buscarPorID(id);
        Habitacion habitacion = reserva == null ? null : habitacionService.buscarPorNumero(reserva.getHabitacion());
        var categoria = habitacion == null ? null : categoriaService.buscarPorID(habitacion.getCategoriaId());

        if (reserva == null || categoria == null || nuevaFechaSalida == null
                || !nuevaFechaSalida.isAfter(reserva.getCheckOut())
                || !habitacionDisponible(reserva.getHabitacion(), reserva.getCheckIn(),
                nuevaFechaSalida, id)) {
            return false;
        }

        long noches = ChronoUnit.DAYS.between(reserva.getCheckIn(), nuevaFechaSalida);
        reserva.setCheckOut(nuevaFechaSalida);
        reserva.setMonto(categoria.getPrecio() * noches);
        if (contarPorCliente(reserva.getCliente()) >= 5) {
            reserva.setMonto(reserva.getMonto() * 0.85);
        }
        guardarReserva(reserva);
        return true;
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