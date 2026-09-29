package com.hotelparaiso.sistema_hotelero.controller;

import com.hotelparaiso.sistema_hotelero.model.Pago;
import com.hotelparaiso.sistema_hotelero.service.PagoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
@Controller
public class PagoController {

    private PagoService pagoService;
    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping("/pagos")
    public String listar(Model model) {
        Pago pago = new Pago();
        pago.setFecha(LocalDate.now());

        model.addAttribute("pagos", pagoService.listar());
        model.addAttribute("pago", pago);
        model.addAttribute("reservasPendientes", pagoService.listarReservasPendientes());
        return "paginas/pagos";
    }

    @PostMapping("/pagos/guardar")
    public String guardar(@ModelAttribute Pago pago, @org.springframework.web.bind.annotation.RequestParam String tipoPago) {
        pagoService.registrarPago(pago, tipoPago);
        return "redirect:/pagos";
    }

    @PostMapping("/pagos/eliminar/{id}")
    public String eliminar(@PathVariable int id) {
        pagoService.eliminarPago(id);
        return "redirect:/pagos";
    }
}