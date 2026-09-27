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

    public boolean existeNumero(String numero) {
        for (Habitacion h : habitaciones) {
            if (h.getNumero().equalsIgnoreCase(numero)) {
                return true;
            }
        }
        return false;
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

    public void eliminar(int id) {
        for (int i = habitaciones.size() - 1; i >= 0; i--) {
            if (habitaciones.get(i).getId() == id) {
                habitaciones.remove(i);
            }
        }
    }
}