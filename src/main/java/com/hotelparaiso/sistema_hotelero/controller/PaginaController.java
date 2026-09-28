// controller/PaginaController.java
package com.hotelparaiso.sistema_hotelero.controller;

import com.hotelparaiso.sistema_hotelero.model.Reserva;
import com.hotelparaiso.sistema_hotelero.service.ClienteService;
import com.hotelparaiso.sistema_hotelero.service.HabitacionService;
import com.hotelparaiso.sistema_hotelero.service.PagoService;
import com.hotelparaiso.sistema_hotelero.service.ReservaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PaginaController {

    private ReservaService reservaService;
    private HabitacionService habitacionService;
    private ClienteService clienteService;
    private PagoService pagoService;

    public PaginaController(ReservaService reservaService,
                            HabitacionService habitacionService,
                            ClienteService clienteService,
                            PagoService pagoService) {
        this.reservaService = reservaService;
        this.habitacionService = habitacionService;
        this.clienteService = clienteService;
        this.pagoService = pagoService;
    }

    @GetMapping("/")
    public String login() {
        return "paginas/login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Reserva> todas = reservaService.listar();
        List<Reserva> ultimas = todas.size() > 5 ? todas.subList(0, 5) : todas;

        model.addAttribute("reservas", ultimas);
        model.addAttribute("reservasActivas", reservaService.contarActivas());
        model.addAttribute("habitacionesDisponibles", habitacionService.listarDisponibles().size());
        model.addAttribute("ingresos", pagoService.totalIngresos());
        model.addAttribute("totalClientes", clienteService.listar().size());
        return "paginas/home";
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