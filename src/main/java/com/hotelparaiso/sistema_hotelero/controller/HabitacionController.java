package com.hotelparaiso.sistema_hotelero.controller;

import com.hotelparaiso.sistema_hotelero.model.Habitacion;
import com.hotelparaiso.sistema_hotelero.service.CategoriaService;
import com.hotelparaiso.sistema_hotelero.service.HabitacionService;
import com.hotelparaiso.sistema_hotelero.service.ReservaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HabitacionController {

    private HabitacionService habitacionService;
    private CategoriaService categoriaService;
    private ReservaService reservaService;

    public HabitacionController(HabitacionService habitacionService, CategoriaService categoriaService,
                                ReservaService reservaService) {
        this.habitacionService = habitacionService;
        this.categoriaService = categoriaService;
        this.reservaService = reservaService;
    }

    @GetMapping("/habitaciones")
    public String listar(Model model) {
        model.addAttribute("habitaciones", habitacionService.listar());
        model.addAttribute("categorias", categoriaService.listar());
        model.addAttribute("habitacion", new Habitacion());
        return "paginas/habitaciones";
    }

    @PostMapping("/habitaciones/guardar")
    public String guardar(@ModelAttribute Habitacion habitacion, RedirectAttributes redirectAttributes) {
        if (!habitacionService.guardarHabitacionValida(habitacion)) {
            redirectAttributes.addFlashAttribute("error", "Ya existe una habitación con ese número.");
            return "redirect:/habitaciones";
        }
        redirectAttributes.addFlashAttribute("exito", "Habitación guardada correctamente.");
        return "redirect:/habitaciones";
    }

    @GetMapping("/habitaciones/eliminar/{id}")
    public String eliminar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        if (!habitacionService.eliminarHabitacion(id, reservaService)) {
            redirectAttributes.addFlashAttribute("error",
                    "Solo se puede eliminar una habitación fuera de servicio y sin reservas.");
            return "redirect:/habitaciones";
        }
        redirectAttributes.addFlashAttribute("exito", "Habitación eliminada.");
        return "redirect:/habitaciones";
    }

}