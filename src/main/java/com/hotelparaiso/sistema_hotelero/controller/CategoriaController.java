package com.hotelparaiso.sistema_hotelero.controller;

import com.hotelparaiso.sistema_hotelero.model.Categoria;
import com.hotelparaiso.sistema_hotelero.service.CategoriaService;
import com.hotelparaiso.sistema_hotelero.service.HabitacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final HabitacionService habitacionService;

    public CategoriaController(CategoriaService categoriaService, HabitacionService habitacionService) {
        this.categoriaService = categoriaService;
        this.habitacionService = habitacionService;
    }

    @GetMapping("/categorias")
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaService.listar());
        model.addAttribute("categoria", new Categoria());
        return "paginas/categorias";
    }

    @PostMapping("/categorias/guardar")
    public String guardar(@ModelAttribute Categoria categoria) {
        categoriaService.guardarCategoria(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/categorias/eliminar/{id}")
    public String eliminar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        if (habitacionService.hayHabitacionesEnCategoria(id)) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar una categoría que tiene habitaciones asignadas.");
            return "redirect:/categorias";
        }
        categoriaService.eliminar(id);
        return "redirect:/categorias";
    }
}