package com.hotelparaiso.sistema_hotelero.controller;

import com.hotelparaiso.sistema_hotelero.service.ReservaService;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    private final ReservaService reservaService;

    public PaginaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("reservas", reservaService.listar());
        model.addAttribute("reservasActivas", reservaService.contarActivas());
        return "paginas/home";
    }

    @GetMapping("/habitaciones")
    public String habitaciones() {
        return "paginas/habitaciones";
    }

    @GetMapping("/clientes")
    public String clientes() {
        return "paginas/clientes";
    }

    @GetMapping("/pagos")
    public String pagos() {
        return "paginas/pagos";
    }

    @GetMapping("/metricas")
    public String metricas() {
        return "paginas/metricas";
    }

    @GetMapping("/publicidad")
    public String publicidad() {
        return "paginas/publicidad";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "paginas/contacto";
    }
}