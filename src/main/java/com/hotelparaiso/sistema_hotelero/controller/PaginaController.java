package com.hotelparaiso.sistema_hotelero.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
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

    @GetMapping("/reservas")
    public String reservas() {
        return "paginas/reservas";
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