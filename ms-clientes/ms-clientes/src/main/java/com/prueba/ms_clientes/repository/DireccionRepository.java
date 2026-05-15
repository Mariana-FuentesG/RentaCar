package com.prueba.ms_clientes.repository;

import com.prueba.ms_clientes.model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DireccionRepository extends JpaRepository<Direccion, Integer> {

    default List<Direccion> findByCiudadContainingIgnoreCase(String ciudad) {
        return null;
    }
}
