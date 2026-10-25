package com.hotelparaiso.sistema_hotelero.adapter;

import com.hotelparaiso.sistema_hotelero.entity.CategoriaEntity;
import com.hotelparaiso.sistema_hotelero.entity.HabitacionEntity;
import com.hotelparaiso.sistema_hotelero.model.Habitacion;
import org.springframework.stereotype.Component;

@Component
public class HabitacionAdapter {

    // Entity -> Model
    public Habitacion toModel(HabitacionEntity entity) {
        if (entity == null) return null;
        Habitacion model = new Habitacion();
        model.setId(entity.getId());
        model.setNumero(entity.getNumero());
        model.setDescripcion(entity.getDescripcion());
        model.setEstado(entity.getEstado());
        model.setCategoriaId(entity.getCategoria() != null ? entity.getCategoria().getId() : 0);
        model.setActivo(entity.getActivo() != null ? entity.getActivo() : true);
        return model;
    }

    // Model -> Entity (recibe la CategoriaEntity, no un número)
    public HabitacionEntity toEntity(Habitacion model, CategoriaEntity categoria) {
        if (model == null) return null;
        HabitacionEntity entity = new HabitacionEntity();
        entity.setId(model.getId());
        entity.setNumero(model.getNumero());
        entity.setDescripcion(model.getDescripcion());
        entity.setEstado(model.getEstado());
        entity.setCategoria(categoria);
        entity.setActivo(model.getActivo() != null ? model.getActivo() : true);
        return entity;
    }
}