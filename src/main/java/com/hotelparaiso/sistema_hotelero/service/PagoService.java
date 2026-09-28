// service/PagoService.java
package com.hotelparaiso.sistema_hotelero.service;

import com.hotelparaiso.sistema_hotelero.model.Pago;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PagoService {

    private List<Pago> pagos = new ArrayList<>();
    private int contadorId = 0;

    public List<Pago> listar() {
        List<Pago> resultado = new ArrayList<>();
        for (int i = pagos.size() - 1; i >= 0; i--) {
            resultado.add(pagos.get(i));
        }
        return resultado;
    }

    public Pago buscarPorID(int id) {
        for (Pago pago : pagos) {
            if (pago.getId() == id) {
                return pago;
            }
        }
        return null;
    }

    public double totalIngresos() {
        double total = 0;
        for (Pago pago : pagos) {
            total += pago.getMonto();
        }
        return total;
    }

    public Pago guardarPago(Pago pago) {
        if (pago.getId() == 0) {
            contadorId++;
            pago.setId(contadorId);
            pagos.add(pago);
        } else {
            for (int i = 0; i < pagos.size(); i++) {
                if (pagos.get(i).getId() == pago.getId()) {
                    pagos.set(i, pago);
                }
            }
        }
        return pago;
    }

    public void eliminar(int id) {
        for (int i = pagos.size() - 1; i >= 0; i--) {
            if (pagos.get(i).getId() == id) {
                pagos.remove(i);
            }
        }
    }
}