package com.hotelparaiso.sistema_hotelero.service;

import com.hotelparaiso.sistema_hotelero.adapter.ClienteAdapter;
import com.hotelparaiso.sistema_hotelero.model.Cliente;
import com.hotelparaiso.sistema_hotelero.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.hotelparaiso.sistema_hotelero.entity.ClienteEntity;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteAdapter clienteAdapter;
    private final ReservaService reservaService;

    public ClienteService(ClienteRepository clienteRepository, ClienteAdapter clienteAdapter,
        ReservaService reservaService) {
        this.clienteRepository = clienteRepository;
        this.clienteAdapter = clienteAdapter;
        this.reservaService = reservaService;
    }

    public List<Cliente> listar() {
        return clienteRepository.findByActivoTrue().stream()
                .map(clienteAdapter::toModel)
                .collect(Collectors.toList());
    }

    public Cliente buscarPorID(int id) {
        return clienteRepository.findById(id)
                .map(clienteAdapter::toModel)
                .orElse(null);
    }

    public Cliente guardarCliente(Cliente cliente) {
        var guardado = clienteRepository.save(clienteAdapter.toEntity(cliente));
        return clienteAdapter.toModel(guardado);
    }

    public void eliminar(int id) {
        ClienteEntity entity = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        entity.setActivo(false);
        clienteRepository.save(entity);
    }

    public boolean eliminarCliente(int id) {
        Cliente cliente = buscarPorID(id);
        if (cliente == null || reservaService.tieneReservasCliente(cliente.getNombre())) {
            return false;
        }
        eliminar(id);
        return true;
    }
}