package com.hotelparaiso.sistema_hotelero.service;

import com.hotelparaiso.sistema_hotelero.adapter.ReservaAdapter;
import com.hotelparaiso.sistema_hotelero.entity.PagoEntity;
import com.hotelparaiso.sistema_hotelero.entity.ReservaEntity;
import com.hotelparaiso.sistema_hotelero.model.Habitacion;
import com.hotelparaiso.sistema_hotelero.model.Reserva;
import com.hotelparaiso.sistema_hotelero.repository.PagoRepository;
import com.hotelparaiso.sistema_hotelero.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final PagoRepository pagoRepository;
    private final ReservaAdapter reservaAdapter;
    private final HabitacionService habitacionService;
    private final CategoriaService categoriaService;

    public ReservaService(ReservaRepository reservaRepository, PagoRepository pagoRepository,
        ReservaAdapter reservaAdapter, HabitacionService habitacionService,
        CategoriaService categoriaService) {
        this.reservaRepository = reservaRepository;
        this.pagoRepository = pagoRepository;
        this.reservaAdapter = reservaAdapter;
        this.habitacionService = habitacionService;
        this.categoriaService = categoriaService;
    }

    public List<Reserva> listar() {
        return reservaRepository.findAllByActivoTrueOrderByIdDesc().stream()
                .map(reservaAdapter::toModel)
                .collect(Collectors.toList());
    }

    public List<Reserva> listarUltimas(int cantidad) {
        List<Reserva> resultado = listar();
        return resultado.size() > cantidad ? resultado.subList(0, cantidad) : resultado;
    }

    public List<Reserva> listarUltimasAsistidas(int cantidad) {
        List<Reserva> resultado = listar().stream()
                .filter(reserva -> "Asistio".equals(reserva.getEstadoReserva()))
                .limit(cantidad)
                .collect(Collectors.toList());
        return resultado;
    }

    public Reserva buscarPorID(int id) {
        return reservaRepository.findById(id)
                .map(reservaAdapter::toModel)
                .orElse(null);
    }

    public int contarActivas() {
        return (int) reservaRepository.countByEstadoReservaNotAndActivoTrue("Cancelada");
    }

    public int contarPorCliente(String cliente) {
        return (int) reservaRepository.countByClienteIgnoreCaseAndEstadoReservaNotAndActivoTrue(cliente, "Cancelada");
    }

    public boolean tieneDescuento(Reserva reserva) {
        if (reserva == null || reserva.getCheckIn() == null || reserva.getCheckOut() == null
                || reserva.getMonto() == null) {
            return false;
        }
        Habitacion habitacion = habitacionService.buscarPorNumero(reserva.getHabitacion());
        var categoria = habitacion == null ? null : categoriaService.buscarPorID(habitacion.getCategoriaId());
        if (categoria == null) {
            return false;
        }
        long noches = ChronoUnit.DAYS.between(reserva.getCheckIn(), reserva.getCheckOut());
        double montoNormal = categoria.getPrecio() * noches;
        return reserva.getMonto() < montoNormal - 0.001;
    }

    public boolean tieneReservasCliente(String cliente) {
        return reservaRepository.existsByClienteIgnoreCaseAndEstadoReservaNotAndActivoTrue(cliente, "Cancelada");
    }

    public boolean tieneReservasHabitacion(String habitacion) {
        return reservaRepository.existsByHabitacionIgnoreCaseAndEstadoReservaNotAndActivoTrue(habitacion, "Cancelada");
    }

    public boolean habitacionDisponible(String habitacion, LocalDate entrada,
                                        LocalDate salida, int idExcluir) {
        for (ReservaEntity reserva : reservaRepository.findByHabitacionIgnoreCaseAndActivoTrue(habitacion)) {
            boolean activa = !"Cancelada".equals(reserva.getEstadoReserva());
            boolean seCruzan = entrada.isBefore(reserva.getCheckOut())
                    && salida.isAfter(reserva.getCheckIn());

            if (reserva.getId() != idExcluir && activa && seCruzan) {
                return false;
            }
        }
        return true;
    }

    public boolean registrarReserva(Reserva reserva) {
        if (reserva.getEstadoReserva() == null) {
            reserva.setEstadoReserva("Reservada");
        }
        if (reserva.getFechaCreacion() == null) {
            reserva.setFechaCreacion(LocalDate.now());
        }
        Habitacion habitacion = habitacionService.buscarPorNumero(reserva.getHabitacion());
        if (habitacion == null || reserva.getCheckIn() == null || reserva.getCheckOut() == null
                || !reserva.getCheckOut().isAfter(reserva.getCheckIn())) {
            return false;
        }

        var categoria = categoriaService.buscarPorID(habitacion.getCategoriaId());
        if (categoria == null || !habitacionDisponible(reserva.getHabitacion(), reserva.getCheckIn(),
                reserva.getCheckOut(), reserva.getId())) {
            return false;
        }

        reserva.setHuespedes(categoria.getCapacidad());
        long noches = ChronoUnit.DAYS.between(reserva.getCheckIn(), reserva.getCheckOut());
        reserva.setMonto(categoria.getPrecio() * noches);
        if (contarPorCliente(reserva.getCliente()) >= 9) {
            reserva.setMonto(reserva.getMonto() * 0.85);
        }

        // Si es una edición, se cargan los datos reales para no perder lo pagado
        if (reserva.getId() != 0) {
            Reserva existente = buscarPorID(reserva.getId());
            if (existente == null) {
                return false;
            }
            existente.setCliente(reserva.getCliente());
            existente.setHabitacion(reserva.getHabitacion());
            existente.setCheckIn(reserva.getCheckIn());
            existente.setCheckOut(reserva.getCheckOut());
            existente.setHuespedes(reserva.getHuespedes());
            existente.setMonto(reserva.getMonto());
            reserva = existente;
        }

        // IMPORTANTE: usar lo que DEVUELVE guardarReserva (ya trae el id generado).
        // Si usas el 'reserva' original (id = 0) se insertaría una fila duplicada.
        Reserva guardada = guardarReserva(reserva);
        actualizarEstadoPorPagos(guardada);
        return true;
    }

    public boolean ampliarReserva(int id, LocalDate nuevaFechaSalida) {
        Reserva reserva = buscarPorID(id);
        Habitacion habitacion = reserva == null ? null : habitacionService.buscarPorNumero(reserva.getHabitacion());
        var categoria = habitacion == null ? null : categoriaService.buscarPorID(habitacion.getCategoriaId());

        if (reserva == null || categoria == null || nuevaFechaSalida == null
                || !nuevaFechaSalida.isAfter(reserva.getCheckOut())
                || !habitacionDisponible(reserva.getHabitacion(), reserva.getCheckIn(),
                nuevaFechaSalida, id)) {
            return false;
        }

        long noches = ChronoUnit.DAYS.between(reserva.getCheckIn(), nuevaFechaSalida);
        reserva.setCheckOut(nuevaFechaSalida);
        reserva.setMonto(categoria.getPrecio() * noches);
        if (contarPorCliente(reserva.getCliente()) >= 10) {
            reserva.setMonto(reserva.getMonto() * 0.85);
        }

        Reserva guardada = guardarReserva(reserva);
        actualizarEstadoPorPagos(guardada);
        return true;
    }

    public Reserva guardarReserva(Reserva reserva) {
        if (reserva.getCheckIn() == null || reserva.getCheckOut() == null
                || !reserva.getCheckOut().isAfter(reserva.getCheckIn())) {
            throw new IllegalArgumentException("La salida debe ser posterior a la fecha de entrada.");
        }
        ReservaEntity guardada = reservaRepository.save(reservaAdapter.toEntity(reserva));
        return reservaAdapter.toModel(guardada);
    }

    @Transactional
    public boolean eliminar(int id) {
        ReservaEntity reserva = reservaRepository.findById(id).orElse(null);
        if (reserva == null) {
            return false;
        }

        for (PagoEntity pago : pagoRepository.findByReserva_Id(id)) {
            pago.setActivo(false);
            pagoRepository.save(pago);
        }
        reserva.setActivo(false);
        reservaRepository.save(reserva);
        return true;
    }

    @Transactional
    public boolean cancelar(int id) {
        ReservaEntity reserva = reservaRepository.findById(id).orElse(null);
        if (reserva == null || "Asistio".equals(reserva.getEstadoReserva())) {
            return false;
        }
        for (PagoEntity pago : pagoRepository.findByReserva_Id(id)) {
            pago.setActivo(false);
            pagoRepository.save(pago);
        }
        reserva.setEstadoReserva("Cancelada");
        reserva.setFechaCancelacion(LocalDate.now());
        reservaRepository.save(reserva);
        return true;
    }

    @Transactional
    public boolean marcarAsistio(int id) {
        ReservaEntity reserva = reservaRepository.findById(id).orElse(null);
        if (reserva == null || !"Reservada".equals(reserva.getEstadoReserva())) {
            return false;
        }
        reserva.setEstadoReserva("Asistio");
        reservaRepository.save(reserva);
        return true;
    }

    public void actualizarEstadoPorPagos(Reserva reserva) {
        double totalPagado = 0;
        for (PagoEntity pago : pagoRepository.findByReserva_IdAndActivoTrue(reserva.getId())) {
            totalPagado += pago.getMonto();
        }
        reserva.setMontoPagado(totalPagado);

        if (!"Cancelada".equals(reserva.getEstadoReserva())) {
            boolean alcanzaAdelanto = totalPagado + 0.001 >= reserva.getMonto() * 0.50;
            reserva.setEstado(alcanzaAdelanto ? "Confirmada" : "Pendiente");
        }
        reservaRepository.save(reservaAdapter.toEntity(reserva));
    }
}