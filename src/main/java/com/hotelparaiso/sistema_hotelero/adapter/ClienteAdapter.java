package com.hotelparaiso.sistema_hotelero.adapter;

import com.hotelparaiso.sistema_hotelero.entity.ClienteEntity;
import com.hotelparaiso.sistema_hotelero.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteAdapter {

    public ClienteEntity toEntity(Cliente model) {
        if (model == null) return null;
        ClienteEntity entity = new ClienteEntity();
        entity.setId(model.getId());
        entity.setNombre(model.getNombre());
        entity.setDocumento(model.getDocumento());
        entity.setTelefono(model.getTelefono());
        entity.setCorreo(model.getCorreo());
        return entity;
    }

    public Cliente toModel(ClienteEntity entity) {
        if (entity == null) return null;
        Cliente model = new Cliente();
        model.setId(entity.getId());
        model.setNombre(entity.getNombre());
        model.setDocumento(entity.getDocumento());
        model.setTelefono(entity.getTelefono());
        model.setCorreo(entity.getCorreo());
        return model;
    }
}