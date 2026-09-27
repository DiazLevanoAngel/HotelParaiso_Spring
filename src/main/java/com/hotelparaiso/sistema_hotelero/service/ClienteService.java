package com.hotelparaiso.sistema_hotelero.service;

import com.hotelparaiso.sistema_hotelero.model.Cliente;
import java.util.ArrayList;
import java.util.List;

public class ClienteService {

    List<Cliente> clientes = new ArrayList<>();

    public void agregar(Cliente cliente) {
        clientes.add(cliente);
    }

    public List<Cliente> listar() {
        return clientes;
    }

    public Cliente buscar(int id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;
    }

    public void eliminar(int id) {
        clientes.removeIf(cliente -> cliente.getId() == id);
    }
}