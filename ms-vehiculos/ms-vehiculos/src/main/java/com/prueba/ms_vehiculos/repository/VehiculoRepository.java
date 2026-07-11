package com.prueba.ms_vehiculos.repository;

import com.prueba.ms_vehiculos.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {

    // QUERY METHOD OBLIGATORIO
    List<Vehiculo> findByDisponibleTrueAndPrecioDiarioLessThan(Double precio);
}