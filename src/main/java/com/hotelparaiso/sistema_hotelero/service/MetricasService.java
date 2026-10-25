package com.hotelparaiso.sistema_hotelero.service;

import com.hotelparaiso.sistema_hotelero.entity.HabitacionEntity;
import com.hotelparaiso.sistema_hotelero.entity.PagoEntity;
import com.hotelparaiso.sistema_hotelero.entity.ReservaEntity;
import com.hotelparaiso.sistema_hotelero.model.Metricas;
import com.hotelparaiso.sistema_hotelero.repository.HabitacionRepository;
import com.hotelparaiso.sistema_hotelero.repository.PagoRepository;
import com.hotelparaiso.sistema_hotelero.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class MetricasService {

    private static final String[] MESES = {
            "Ene", "Feb", "Mar", "Abr", "May", "Jun",
            "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
    };

    private final HabitacionRepository habitacionRepository;
    private final PagoRepository pagoRepository;
    private final ReservaRepository reservaRepository;

    public MetricasService(HabitacionRepository habitacionRepository,
                           PagoRepository pagoRepository,
                           ReservaRepository reservaRepository) {
        this.habitacionRepository = habitacionRepository;
        this.pagoRepository = pagoRepository;
        this.reservaRepository = reservaRepository;
    }

    public Metricas obtenerMetricas() {
        List<HabitacionEntity> habitaciones = habitacionRepository.findByActivoTrue();
        List<PagoEntity> pagos = pagoRepository.findByActivoTrue();
        List<ReservaEntity> reservas = reservaRepository.findAll();

        List<String> etiquetas = new ArrayList<>();
        List<Double> ocupacion = new ArrayList<>();
        List<Double> ingresos = new ArrayList<>();
        List<Long> reservasMensuales = new ArrayList<>();
        List<Long> cancelaciones = new ArrayList<>();

        LocalDate inicioSemanaActual = LocalDate.now()
                .with(java.time.temporal.TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        for (int offset = 5; offset >= 0; offset--) {
            LocalDate inicioSemana = inicioSemanaActual.minusWeeks(offset);
            LocalDate finSemana = inicioSemana.plusDays(7);
            etiquetas.add(inicioSemana.getDayOfMonth() + " " + MESES[inicioSemana.getMonthValue() - 1]);
            ocupacion.add(calcularOcupacion(inicioSemana, finSemana, habitaciones, reservas));
            ingresos.add(calcularIngresos(inicioSemana, finSemana, pagos));
            reservasMensuales.add(contarReservas(inicioSemana, finSemana, reservas));
            cancelaciones.add(contarCancelaciones(inicioSemana, finSemana, reservas));
        }

        return new Metricas(etiquetas, ocupacion, ingresos, reservasMensuales, cancelaciones);
    }

    private double calcularOcupacion(LocalDate inicioSemana, LocalDate finSemana,
                                     List<HabitacionEntity> habitaciones,
                                     List<ReservaEntity> reservas) {
        long capacidad = (long) habitaciones.size() * 7;
        if (capacidad == 0) {
            return 0;
        }

        long nochesOcupadas = 0;
        for (ReservaEntity reserva : reservas) {
            if (!Boolean.TRUE.equals(reserva.getActivo())
                    || "Cancelada".equals(reserva.getEstadoReserva())
                    || reserva.getCheckIn() == null || reserva.getCheckOut() == null) {
                continue;
            }
            LocalDate inicio = reserva.getCheckIn().isAfter(inicioSemana)
                    ? reserva.getCheckIn() : inicioSemana;
            LocalDate fin = reserva.getCheckOut().isBefore(finSemana)
                    ? reserva.getCheckOut() : finSemana;
            if (inicio.isBefore(fin)) {
                nochesOcupadas += ChronoUnit.DAYS.between(inicio, fin);
            }
        }
        return redondear((double) nochesOcupadas * 100 / capacidad);
    }

    private double calcularIngresos(LocalDate inicioSemana, LocalDate finSemana, List<PagoEntity> pagos) {
        double total = 0;
        for (PagoEntity pago : pagos) {
            if (pago.getFecha() != null && !pago.getFecha().isBefore(inicioSemana)
                    && pago.getFecha().isBefore(finSemana)
                    && pago.getMonto() != null) {
                total += pago.getMonto();
            }
        }
        return redondear(total);
    }

    private long contarReservas(LocalDate inicioSemana, LocalDate finSemana,
                                List<ReservaEntity> reservas) {
        return reservas.stream()
                .filter(reserva -> reserva.getFechaCreacion() != null
                        && !reserva.getFechaCreacion().isBefore(inicioSemana)
                        && reserva.getFechaCreacion().isBefore(finSemana))
                .count();
    }

    private long contarCancelaciones(LocalDate inicioSemana, LocalDate finSemana,
                                     List<ReservaEntity> reservas) {
        return reservas.stream()
                .filter(reserva -> "Cancelada".equals(reserva.getEstadoReserva())
                        && reserva.getFechaCancelacion() != null
                        && !reserva.getFechaCancelacion().isBefore(inicioSemana)
                        && reserva.getFechaCancelacion().isBefore(finSemana))
                .count();
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
