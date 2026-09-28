package com.hotelparaiso.sistema_hotelero.controller;

import com.hotelparaiso.sistema_hotelero.model.Reserva;
import com.hotelparaiso.sistema_hotelero.service.ReservaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping("/reservas")
    public String listar(Model model) {
        model.addAttribute("reservas", reservaService.listar());
        model.addAttribute("reserva", new Reserva());
        return "paginas/reservas";
    }

    @PostMapping("/reservas/guardar")
    public String guardar(@ModelAttribute Reserva reserva, RedirectAttributes redirectAttributes) {
        try {
            reservaService.guardar(reserva);
            redirectAttributes.addFlashAttribute("mensaje", "La reserva se guardó correctamente.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/reservas";
    }

    @PostMapping("/reservas/eliminar/{id}")
    public String eliminar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        if (reservaService.eliminar(id)) {
            redirectAttributes.addFlashAttribute("mensaje", "La reserva se eliminó correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró la reserva que intentas eliminar.");
        }
        return "redirect:/reservas";
    }

    @PostMapping("/reservas/cancelar/{id}")
    public String cancelar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            reservaService.cancelar(id);
            redirectAttributes.addFlashAttribute("mensaje", "La reserva se canceló correctamente.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/reservas";
    }
}
