package com.hotelparaiso.sistema_hotelero.service;

import com.hotelparaiso.sistema_hotelero.adapter.CategoriaAdapter;
import com.hotelparaiso.sistema_hotelero.model.Categoria;
import com.hotelparaiso.sistema_hotelero.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaAdapter categoriaAdapter;

    public CategoriaService(CategoriaRepository categoriaRepository, CategoriaAdapter categoriaAdapter) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaAdapter = categoriaAdapter;
    }

    public List<Categoria> listar() {
        return categoriaRepository.findAll().stream()
                .map(categoriaAdapter::toModel)
                .collect(Collectors.toList());
    }

    public Categoria buscarPorID(int id) {
        return categoriaRepository.findById(id)
                .map(categoriaAdapter::toModel)
                .orElse(null);
    }

    public boolean existeNombre(String nombre, int idExcluir) {
        return categoriaRepository.existsByNombreIgnoreCaseAndIdNot(nombre, idExcluir);
    }

    public Categoria guardarCategoria(Categoria categoria) {
        var guardada = categoriaRepository.save(categoriaAdapter.toEntity(categoria));
        return categoriaAdapter.toModel(guardada);
    }

    public void eliminar(int id) {
        categoriaRepository.deleteById(id);
    }
}