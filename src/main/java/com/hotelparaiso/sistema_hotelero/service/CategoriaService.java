package com.hotelparaiso.sistema_hotelero.service;

import com.hotelparaiso.sistema_hotelero.model.Categoria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriaService {

    private List<Categoria> categorias = new ArrayList<>();

    private int contadorId = 0;

    public List<Categoria> listar() {
        return categorias;
    }

    public Categoria buscarPorID(int id) {
        for (Categoria categoria : categorias) {
            if (categoria.getId() == id) {
                return categoria;
            }
        }
        return null;
    }

    public boolean existeNombre(String nombre, int idExcluir) {
        for (Categoria categoria : categorias) {
            if (categoria.getId() != idExcluir && categoria.getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    public Categoria guardarCategoria(Categoria categoria) {
        if (categoria.getId() == 0) {
            contadorId++;
            categoria.setId(contadorId);
            categorias.add(categoria);
        } else {
            for (int i = 0; i < categorias.size(); i++) {
                if (categorias.get(i).getId() == categoria.getId()) {
                    categorias.set(i, categoria);
                }
            }
        }
        return categoria;
    }

    public void eliminar(int id) {
        for (int i = categorias.size() - 1; i >= 0; i--) {
            if (categorias.get(i).getId() == id) {
                categorias.remove(i);
            }
        }
    }
}