package com.hotelparaiso.sistema_hotelero.controller;

import com.hotelparaiso.sistema_hotelero.model.Cliente;
import com.hotelparaiso.sistema_hotelero.service.ClienteService;
import java.util.List;

public class ClienteController {

    ClienteService service = new ClienteService();

    public void agregar(Cliente cliente) {
        service.agregar(cliente);
    }

    public List<Cliente> listar() {
        return service.listar();
    }

    public Cliente buscar(int id) {
        return service.buscar(id);
    }

    public void eliminar(int id) {
        service.eliminar(id);
    }
}