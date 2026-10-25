package com.hotelparaiso.sistema_hotelero.service;

import com.hotelparaiso.sistema_hotelero.adapter.HabitacionAdapter;
import com.hotelparaiso.sistema_hotelero.entity.CategoriaEntity;
import com.hotelparaiso.sistema_hotelero.model.Habitacion;
import com.hotelparaiso.sistema_hotelero.repository.CategoriaRepository;
import com.hotelparaiso.sistema_hotelero.repository.HabitacionRepository;
import org.springframework.stereotype.Service;

import com.hotelparaiso.sistema_hotelero.entity.HabitacionEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final CategoriaRepository categoriaRepository;
    private final HabitacionAdapter habitacionAdapter;

    public HabitacionService(HabitacionRepository habitacionRepository,
        CategoriaRepository categoriaRepository,
        HabitacionAdapter habitacionAdapter) {
        this.habitacionRepository = habitacionRepository;
        this.categoriaRepository = categoriaRepository;
        this.habitacionAdapter = habitacionAdapter;
    }

    public List<Habitacion> listar() {
        return habitacionRepository.findByActivoTrue().stream()
                .map(habitacionAdapter::toModel)
                .collect(Collectors.toList());
    }

    public Habitacion buscarPorID(int id) {
        return habitacionRepository.findById(id)
                .map(habitacionAdapter::toModel)
                .orElse(null);
    }

    public boolean existeNumero(String numero, int idExcluir) {
        return habitacionRepository.existsByNumeroIgnoreCaseAndIdNotAndActivoTrue(numero, idExcluir);
    }

    public boolean existeNumero(String numero) {
        return existeNumero(numero, 0);
    }

    public boolean hayHabitacionesEnCategoria(int categoriaId) {
        return habitacionRepository.existsByCategoria_IdAndActivoTrue(categoriaId);
    }

    public int contarDisponibles() {
        return listarDisponibles().size();
    }

    public List<Habitacion> listarDisponibles() {
        return habitacionRepository.findByEstadoIgnoreCaseAndActivoTrue("Disponible").stream()
                .map(habitacionAdapter::toModel)
                .collect(Collectors.toList());
    }

    public Habitacion guardarHabitacion(Habitacion habitacion) {
        // 1) buscar la CategoriaEntity (igual que TaskService del profesor)
        CategoriaEntity categoria = categoriaRepository.findById(habitacion.getCategoriaId())
                .orElseThrow(() -> new RuntimeException(
                        "Categoría no encontrada con ID: " + habitacion.getCategoriaId()));
        // 2) adapter -> repository -> adapter
        var guardada = habitacionRepository.save(habitacionAdapter.toEntity(habitacion, categoria));
        return habitacionAdapter.toModel(guardada);
    }

    public boolean guardarHabitacionValida(Habitacion habitacion) {
        if (existeNumero(habitacion.getNumero(), habitacion.getId())) {
            return false;
        }
        guardarHabitacion(habitacion);
        return true;
    }

    public Habitacion buscarPorNumero(String numero) {
        return habitacionRepository.findByNumeroIgnoreCaseAndActivoTrue(numero)
                .map(habitacionAdapter::toModel)
                .orElse(null);
    }

    public void eliminar(int id) {
    HabitacionEntity entity = habitacionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Habitación no encontrada con ID: " + id));
    entity.setActivo(false);
    habitacionRepository.save(entity);
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