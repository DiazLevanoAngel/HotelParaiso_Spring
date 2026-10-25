package com.hotelparaiso.sistema_hotelero.adapter;

import com.hotelparaiso.sistema_hotelero.entity.CategoriaEntity;
import com.hotelparaiso.sistema_hotelero.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaAdapter {

    public CategoriaEntity toEntity(Categoria model) {
        if (model == null) return null;
        CategoriaEntity entity = new CategoriaEntity();
        entity.setId(model.getId());
        entity.setNombre(model.getNombre());
        entity.setDescripcion(model.getDescripcion());
        entity.setCapacidad(model.getCapacidad());
        entity.setPrecio(model.getPrecio());
        entity.setEstado(model.getEstado());
        entity.setActivo(model.getActivo() != null ? model.getActivo() : true);
        return entity;
    }

    public Categoria toModel(CategoriaEntity entity) {
        if (entity == null) return null;
        Categoria model = new Categoria();
        model.setId(entity.getId());
        model.setNombre(entity.getNombre());
        model.setDescripcion(entity.getDescripcion());
        model.setCapacidad(entity.getCapacidad());
        model.setPrecio(entity.getPrecio());
        model.setEstado(entity.getEstado());
        model.setActivo(entity.getActivo() != null ? entity.getActivo() : true);
        return model;
    }
}