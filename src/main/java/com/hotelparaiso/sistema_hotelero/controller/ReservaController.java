// controller/ReservaController.java
package com.hotelparaiso.sistema_hotelero.controller;

import com.hotelparaiso.sistema_hotelero.model.Habitacion;
import com.hotelparaiso.sistema_hotelero.model.Reserva;
import com.hotelparaiso.sistema_hotelero.service.ClienteService;
import com.hotelparaiso.sistema_hotelero.service.CategoriaService;
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
    private CategoriaService categoriaService;

    public ReservaController(ReservaService reservaService,
                             ClienteService clienteService,
                             HabitacionService habitacionService,
                             CategoriaService categoriaService) {
        this.reservaService = reservaService;
        this.clienteService = clienteService;
        this.habitacionService = habitacionService;
        this.categoriaService = categoriaService;
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
            var categoria = categoriaService.buscarPorID(habitacion.getCategoriaId());
            if (categoria == null) {
                return "redirect:/reservas";
            }
            reserva.setHuespedes(categoria.getCapacidad());
            boolean fechasLibres = reservaService.habitacionDisponible(
                    reserva.getHabitacion(), reserva.getCheckIn(), reserva.getCheckOut(), reserva.getId());
            if (!fechasLibres) {
                return "redirect:/reservas";
            }
            long noches = ChronoUnit.DAYS.between(reserva.getCheckIn(), reserva.getCheckOut());
            reserva.setMonto(categoria.getPrecio() * noches);
            if (reservaService.contarPorCliente(reserva.getCliente()) >= 4) {
                reserva.setMonto(reserva.getMonto() * 0.85);
            }
            reservaService.guardarReserva(reserva);
        }
        return "redirect:/reservas";
    }

    @PostMapping("/reservas/ampliar/{id}")
    public String ampliar(@PathVariable int id, @ModelAttribute Reserva datos) {
        Reserva reserva = reservaService.buscarPorID(id);
        Habitacion habitacion = reserva == null ? null : habitacionService.buscarPorNumero(reserva.getHabitacion());
        var categoria = habitacion == null ? null : categoriaService.buscarPorID(habitacion.getCategoriaId());

        if (reserva != null && habitacion != null && categoria != null && datos.getCheckOut() != null
                && datos.getCheckOut().isAfter(reserva.getCheckOut())
                && reservaService.habitacionDisponible(reserva.getHabitacion(), reserva.getCheckIn(),
                datos.getCheckOut(), id)) {
            long noches = ChronoUnit.DAYS.between(reserva.getCheckIn(), datos.getCheckOut());
            reserva.setCheckOut(datos.getCheckOut());
            reserva.setMonto(categoria.getPrecio() * noches);
            if (reservaService.contarPorCliente(reserva.getCliente()) >= 5) {
                reserva.setMonto(reserva.getMonto() * 0.85);
            }
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