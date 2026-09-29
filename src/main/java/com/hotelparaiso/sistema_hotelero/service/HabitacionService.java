package com.hotelparaiso.sistema_hotelero.service;

import com.hotelparaiso.sistema_hotelero.model.Habitacion;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class HabitacionService {

    private List<Habitacion> habitaciones = new ArrayList<>();
    private int contadorId = 0;

    public List<Habitacion> listar() {
        return habitaciones;
    }

    public Habitacion buscarPorID(int id) {
        for (Habitacion h : habitaciones) {
            if (h.getId() == id)
                return h;
        }
        return null;
    }

    public boolean existeNumero(String numero, int idExcluir) {
        for (Habitacion h : habitaciones) {
            if (h.getId() != idExcluir && h.getNumero().equalsIgnoreCase(numero)) {
                return true;
            }
        }
        return false;
    }

    public boolean existeNumero(String numero) {
        return existeNumero(numero, 0);
    }

    public boolean hayHabitacionesEnCategoria(int categoriaId) {
        for (Habitacion h : habitaciones) {
            if (h.getCategoriaId() == categoriaId) {
                return true;
            }
        }
        return false;
    }

    public int contarDisponibles() {
        return listarDisponibles().size();
    }

    public List<Habitacion> listarDisponibles() {
        List<Habitacion> disponibles = new ArrayList<>();
        for (Habitacion h : habitaciones) {
            if ("Disponible".equalsIgnoreCase(h.getEstado())) {
                disponibles.add(h);
            }
        }
        return disponibles;
    }

    public Habitacion guardarHabitacion(Habitacion habitacion) {
        if (habitacion.getId() == 0) {
            contadorId++;
            habitacion.setId(contadorId);
            habitaciones.add(habitacion);
        } else {
            for (int i = 0; i < habitaciones.size(); i++) {
                if (habitaciones.get(i).getId() == habitacion.getId()) {
                    habitaciones.set(i, habitacion);
                }
            }
        }
        return habitacion;
    }

    public boolean guardarHabitacionValida(Habitacion habitacion) {
        if (habitacion.getId() == 0 && existeNumero(habitacion.getNumero())) {
            return false;
        }
        guardarHabitacion(habitacion);
        return true;
    }

    public Habitacion buscarPorNumero(String numero) {
        for (Habitacion h : habitaciones) {
            if (h.getNumero().equalsIgnoreCase(numero)) {
                return h;
            }
        }
        return null;
    }

    public void eliminar(int id) {
        for (int i = habitaciones.size() - 1; i >= 0; i--) {
            if (habitaciones.get(i).getId() == id) {
                habitaciones.remove(i);
            }
        }
    }

    public boolean eliminarHabitacion(int id, ReservaService reservaService) {
        Habitacion habitacion = buscarPorID(id);
        if (habitacion == null || !"Fuera de servicio".equalsIgnoreCase(habitacion.getEstado())
                || reservaService.tieneReservasHabitacion(habitacion.getNumero())) {
            return false;
        }
        eliminar(id);
        return true;
    }
}