package com.hotelparaiso.sistema_hotelero.controller;

import com.hotelparaiso.sistema_hotelero.service.ClienteService;
import com.hotelparaiso.sistema_hotelero.service.HabitacionService;
import com.hotelparaiso.sistema_hotelero.service.PagoService;
import com.hotelparaiso.sistema_hotelero.service.ReservaService;
import com.hotelparaiso.sistema_hotelero.service.MetricasService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    private ReservaService reservaService;
    private HabitacionService habitacionService;
    private ClienteService clienteService;
    private PagoService pagoService;
    private MetricasService metricasService;

    public PaginaController(ReservaService reservaService,
                            HabitacionService habitacionService,
                            ClienteService clienteService,
                            PagoService pagoService,
                            MetricasService metricasService) {
        this.reservaService = reservaService;
        this.habitacionService = habitacionService;
        this.clienteService = clienteService;
        this.pagoService = pagoService;
        this.metricasService = metricasService;
    }

    @GetMapping("/")
    public String login() {
        return "paginas/login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("reservas", reservaService.listarUltimasAsistidas(5));
        model.addAttribute("reservasActivas", reservaService.contarActivas());
        model.addAttribute("habitacionesDisponibles", habitacionService.listarDisponibles().size());
        model.addAttribute("ingresos", pagoService.totalIngresos());
        model.addAttribute("totalClientes", clienteService.listar().size());
        return "paginas/home";
    }

    @GetMapping("/metricas")
    public String metricas(Model model) {
        var metricas = metricasService.obtenerMetricas();
        model.addAttribute("etiquetasMetricas", metricas.getEtiquetas());
        model.addAttribute("ocupacionMensual", metricas.getOcupacion());
        model.addAttribute("ingresosMensuales", metricas.getIngresos());
        model.addAttribute("reservasMensuales", metricas.getReservas());
        model.addAttribute("cancelacionesMensuales", metricas.getCancelaciones());
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