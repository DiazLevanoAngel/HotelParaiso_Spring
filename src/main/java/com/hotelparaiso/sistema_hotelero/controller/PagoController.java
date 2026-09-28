// controller/PagoController.java
package com.hotelparaiso.sistema_hotelero.controller;

import com.hotelparaiso.sistema_hotelero.model.Pago;
import com.hotelparaiso.sistema_hotelero.model.Reserva;
import com.hotelparaiso.sistema_hotelero.service.PagoService;
import com.hotelparaiso.sistema_hotelero.service.ReservaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PagoController {

    private PagoService pagoService;
    private ReservaService reservaService;

    public PagoController(PagoService pagoService, ReservaService reservaService) {
        this.pagoService = pagoService;
        this.reservaService = reservaService;
    }

    @GetMapping("/pagos")
    public String listar(Model model) {
        Pago pago = new Pago();
        pago.setFecha(LocalDate.now());

        List<Reserva> pendientes = new ArrayList<>();
        for (Reserva r : reservaService.listar()) {
            if (r.getMontoPagado() < r.getMonto()) {
                pendientes.add(r);
            }
        }

        model.addAttribute("pagos", pagoService.listar());
        model.addAttribute("pago", pago);
        model.addAttribute("reservasPendientes", pendientes);
        return "paginas/pagos";
    }

    @PostMapping("/pagos/guardar")
    public String guardar(@ModelAttribute Pago pago, @org.springframework.web.bind.annotation.RequestParam String tipoPago) {
        Reserva reserva = reservaService.buscarPorID(pago.getReservaId());

        if (reserva != null && reserva.getMontoPagado() < reserva.getMonto()) {
            double falta = reserva.getMonto() - reserva.getMontoPagado();
            double adelanto = reserva.getMonto() * 0.50 - reserva.getMontoPagado();
            pago.setMonto("adelanto".equals(tipoPago) ? Math.max(0, adelanto) : falta);
            if (pago.getMonto() <= 0) {
                return "redirect:/pagos";
            }
            Pago pagoRegistrado = pagoService.buscarPorReserva(reserva.getId());
            if (pagoRegistrado == null) {
                pago.setId(0);
                pago.setCliente(reserva.getCliente());
                pago.setHabitacion(reserva.getHabitacion());
                pagoService.guardarPago(pago);
            } else {
                pagoRegistrado.setMonto(pagoRegistrado.getMonto() + pago.getMonto());
                pagoRegistrado.setMetodoPago(pago.getMetodoPago());
                pagoRegistrado.setFecha(pago.getFecha());
                pagoService.guardarPago(pagoRegistrado);
            }

            reserva.setMontoPagado(reserva.getMontoPagado() + pago.getMonto());
            if (reserva.getMontoPagado() >= reserva.getMonto() * 0.50) {
                reserva.setEstado("Confirmada");
            }
        }
        return "redirect:/pagos";
    }

    @PostMapping("/pagos/eliminar/{id}")
    public String eliminar(@PathVariable int id) {
        Pago pago = pagoService.buscarPorID(id);
        if (pago != null) {
            Reserva reserva = reservaService.buscarPorID(pago.getReservaId());
            if (reserva != null) {
                reserva.setMontoPagado(Math.max(0, reserva.getMontoPagado() - pago.getMonto()));
                if (reserva.getMontoPagado() >= reserva.getMonto() * 0.50) {
                    reserva.setEstado("Confirmada");
                } else {
                    reserva.setEstado("Pendiente");
                }
            }
            pagoService.eliminar(id);
        }
        return "redirect:/pagos";
    }
}