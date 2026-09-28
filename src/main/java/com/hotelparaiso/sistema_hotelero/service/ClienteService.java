// service/ClienteService.java
package com.hotelparaiso.sistema_hotelero.service;

import com.hotelparaiso.sistema_hotelero.model.Cliente;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    private List<Cliente> clientes = new ArrayList<>();
    private int contadorId = 0;

    public List<Cliente> listar() {
        return clientes;
    }

    public Cliente buscarPorID(int id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;
    }

    public Cliente guardarCliente(Cliente cliente) {
        if (cliente.getId() == 0) {
            contadorId++;
            cliente.setId(contadorId);
            clientes.add(cliente);
        } else {
            for (int i = 0; i < clientes.size(); i++) {
                if (clientes.get(i).getId() == cliente.getId()) {
                    clientes.set(i, cliente);
                }
            }
        }
        return cliente;
    }

    public void eliminar(int id) {
        for (int i = clientes.size() - 1; i >= 0; i--) {
            if (clientes.get(i).getId() == id) {
                clientes.remove(i);
            }
        }
    }
}