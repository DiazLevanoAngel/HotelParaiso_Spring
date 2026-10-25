package com.hotelparaiso.sistema_hotelero.model;

import java.util.List;

public class Metricas {

    private final List<String> etiquetas;
    private final List<Double> ocupacion;
    private final List<Double> ingresos;
    private final List<Long> reservas;
    private final List<Long> cancelaciones;

    public Metricas(List<String> etiquetas, List<Double> ocupacion, List<Double> ingresos,
                    List<Long> reservas, List<Long> cancelaciones) {
        this.etiquetas = etiquetas;
        this.ocupacion = ocupacion;
        this.ingresos = ingresos;
        this.reservas = reservas;
        this.cancelaciones = cancelaciones;
    }

    public List<String> getEtiquetas() { return etiquetas; }
    public List<Double> getOcupacion() { return ocupacion; }
    public List<Double> getIngresos() { return ingresos; }
    public List<Long> getReservas() { return reservas; }
    public List<Long> getCancelaciones() { return cancelaciones; }
}
