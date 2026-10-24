package com.hotelparaiso.sistema_hotelero.service;

import com.hotelparaiso.sistema_hotelero.adapter.PagoAdapter;
import com.hotelparaiso.sistema_hotelero.entity.PagoEntity;
import com.hotelparaiso.sistema_hotelero.entity.ReservaEntity;
import com.hotelparaiso.sistema_hotelero.model.Pago;
import com.hotelparaiso.sistema_hotelero.model.Reserva;
import com.hotelparaiso.sistema_hotelero.repository.PagoRepository;
import com.hotelparaiso.sistema_hotelero.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ReservaRepository reservaRepository;
    private final PagoAdapter pagoAdapter;
    private final ReservaService reservaService;

    public PagoService(PagoRepository pagoRepository, ReservaRepository reservaRepository,
                       PagoAdapter pagoAdapter, ReservaService reservaService) {
        this.pagoRepository = pagoRepository;
        this.reservaRepository = reservaRepository;
        this.pagoAdapter = pagoAdapter;
        this.reservaService = reservaService;
    }

    public List<Pago> listar() {
        return pagoRepository.findAllByOrderByIdDesc().stream()
                .map(pagoAdapter::toModel)
                .collect(Collectors.toList());
    }

    public List<Reserva> listarReservasPendientes() {
        List<Reserva> pendientes = new ArrayList<>();
        for (Reserva reserva : reservaService.listar()) {
            if (reserva.getMontoPagado() < reserva.getMonto()) {
                pendientes.add(reserva);
            }
        }
        return pendientes;
    }

    @Transactional
    public boolean registrarPago(Pago pago, String tipoPago) {
        Reserva reserva = reservaService.buscarPorID(pago.getReservaId());
        if (reserva == null || reserva.getMontoPagado() >= reserva.getMonto()) {
            return false;
        }

        double falta = reserva.getMonto() - reserva.getMontoPagado();
        double adelanto = reserva.getMonto() * 0.50 - reserva.getMontoPagado();
        pago.setMonto("adelanto".equals(tipoPago) ? Math.max(0, adelanto) : falta);
        if (pago.getMonto() <= 0) {
            return false;
        }

        pago.setId(0);
        pago.setCliente(reserva.getCliente());
        pago.setHabitacion(reserva.getHabitacion());

        // Se busca la ReservaEntity (igual que se busca la CategoriaEntity en Habitación)
        ReservaEntity reservaEntity = reservaRepository.findById(reserva.getId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + reserva.getId()));
        pagoRepository.save(pagoAdapter.toEntity(pago, reservaEntity));

        reservaService.actualizarEstadoPorPagos(reserva);
        return true;
    }

    @Transactional
    public boolean eliminarPago(int id) {
        PagoEntity pago = pagoRepository.findById(id).orElse(null);
        if (pago == null) {
            return false;
        }
        int reservaId = pago.getReserva() != null ? pago.getReserva().getId() : 0;
        pagoRepository.delete(pago);

        Reserva reserva = reservaService.buscarPorID(reservaId);
        if (reserva != null) {
            reservaService.actualizarEstadoPorPagos(reserva);
        }
        return true;
    }

    public Pago buscarPorID(int id) {
        return pagoRepository.findById(id)
                .map(pagoAdapter::toModel)
                .orElse(null);
    }

    public double totalIngresos() {
        double total = 0;
        for (PagoEntity pago : pagoRepository.findAll()) {
            total += pago.getMonto();
        }
        return total;
    }

    public void eliminar(int id) {
        pagoRepository.deleteById(id);
    }
}