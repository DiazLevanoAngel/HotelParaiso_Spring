package com.hotelparaiso.sistema_hotelero.adapter;

import com.hotelparaiso.sistema_hotelero.entity.PagoEntity;
import com.hotelparaiso.sistema_hotelero.entity.ReservaEntity;
import com.hotelparaiso.sistema_hotelero.model.Pago;
import org.springframework.stereotype.Component;

@Component
public class PagoAdapter {

    public Pago toModel(PagoEntity entity) {
        if (entity == null) return null;
        Pago model = new Pago();
        model.setId(entity.getId());
        model.setReservaId(entity.getReserva() != null ? entity.getReserva().getId() : 0);
        model.setCliente(entity.getCliente());
        model.setHabitacion(entity.getHabitacion());
        model.setMonto(entity.getMonto());
        model.setMetodoPago(entity.getMetodoPago());
        model.setFecha(entity.getFecha());
        return model;
    }

    public PagoEntity toEntity(Pago model, ReservaEntity reserva) {
        if (model == null) return null;
        PagoEntity entity = new PagoEntity();
        entity.setId(model.getId());
        entity.setReserva(reserva);
        entity.setCliente(model.getCliente());
        entity.setHabitacion(model.getHabitacion());
        entity.setMonto(model.getMonto());
        entity.setMetodoPago(model.getMetodoPago());
        entity.setFecha(model.getFecha());
        return entity;
    }
}