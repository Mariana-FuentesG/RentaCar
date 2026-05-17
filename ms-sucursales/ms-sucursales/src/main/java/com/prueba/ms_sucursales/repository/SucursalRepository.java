package com.prueba.ms_sucursales.repository;

import com.prueba.ms_sucursales.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {

    // QUERY METHOD
    List<Sucursal> findByCiudadContainingIgnoreCase(String ciudad);
}