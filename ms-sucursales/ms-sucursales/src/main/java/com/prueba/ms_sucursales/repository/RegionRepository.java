package com.prueba.ms_sucursales.repository;

import com.prueba.ms_sucursales.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Integer> {

    // QUERY METHOD
    List<Region> findByNombreContainingIgnoreCase(String nombre);
}