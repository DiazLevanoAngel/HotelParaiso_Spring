package com.hotelparaiso.sistema_hotelero.adapter;

import com.hotelparaiso.sistema_hotelero.entity.ReservaEntity;
import com.hotelparaiso.sistema_hotelero.model.Reserva;
import org.springframework.stereotype.Component;

@Component
public class ReservaAdapter {

    public Reserva toModel(ReservaEntity entity) {
        if (entity == null) return null;
        Reserva model = new Reserva();
        model.setId(entity.getId());
        model.setCliente(entity.getCliente());
        model.setHabitacion(entity.getHabitacion());
        model.setCheckIn(entity.getCheckIn());
        model.setCheckOut(entity.getCheckOut());
        model.setHuespedes(entity.getHuespedes());
        model.setMonto(entity.getMonto());
        model.setMontoPagado(entity.getMontoPagado());
        model.setEstado(entity.getEstado());
        model.setActivo(entity.getActivo() != null ? entity.getActivo() : true);
        return model;
    }

    public ReservaEntity toEntity(Reserva model) {
        if (model == null) return null;
        ReservaEntity entity = new ReservaEntity();
        entity.setId(model.getId());
        entity.setCliente(model.getCliente());
        entity.setHabitacion(model.getHabitacion());
        entity.setCheckIn(model.getCheckIn());
        entity.setCheckOut(model.getCheckOut());
        entity.setHuespedes(model.getHuespedes());
        entity.setMonto(model.getMonto());
        entity.setMontoPagado(model.getMontoPagado());
        entity.setEstado(model.getEstado());
        entity.setActivo(model.getActivo() != null ? model.getActivo() : true);
        return entity;
    }
}