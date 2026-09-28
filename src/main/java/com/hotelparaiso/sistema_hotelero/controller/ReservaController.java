// controller/ReservaController.java
package com.hotelparaiso.sistema_hotelero.controller;

import com.hotelparaiso.sistema_hotelero.model.Habitacion;
import com.hotelparaiso.sistema_hotelero.model.Reserva;
import com.hotelparaiso.sistema_hotelero.service.ClienteService;
import com.hotelparaiso.sistema_hotelero.service.HabitacionService;
import com.hotelparaiso.sistema_hotelero.service.ReservaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.temporal.ChronoUnit;

@Controller
public class ReservaController {

    private ReservaService reservaService;
    private ClienteService clienteService;
    private HabitacionService habitacionService;

    public ReservaController(ReservaService reservaService,
                             ClienteService clienteService,
                             HabitacionService habitacionService) {
        this.reservaService = reservaService;
        this.clienteService = clienteService;
        this.habitacionService = habitacionService;
    }

    @GetMapping("/reservas")
    public String listar(Model model) {
        model.addAttribute("reservas", reservaService.listar());
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("clientes", clienteService.listar());
        model.addAttribute("habitaciones", habitacionService.listar());
        return "paginas/reservas";
    }

    @PostMapping("/reservas/guardar")
    public String guardar(@ModelAttribute Reserva reserva) {
        Habitacion habitacion = habitacionService.buscarPorNumero(reserva.getHabitacion());
        boolean fechasOk = reserva.getCheckIn() != null && reserva.getCheckOut() != null
                && reserva.getCheckOut().isAfter(reserva.getCheckIn());

        if (habitacion != null && fechasOk) {
            long noches = ChronoUnit.DAYS.between(reserva.getCheckIn(), reserva.getCheckOut());
            reserva.setMonto(habitacion.getPrecio() * noches);
            reservaService.guardarReserva(reserva);
        }
        return "redirect:/reservas";
    }

    @PostMapping("/reservas/eliminar/{id}")
    public String eliminar(@PathVariable int id) {
        reservaService.eliminar(id);
        return "redirect:/reservas";
    }
}