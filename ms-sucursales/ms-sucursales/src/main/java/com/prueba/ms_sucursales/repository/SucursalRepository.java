package com.prueba.ms_sucursales.repository;

import com.prueba.ms_sucursales.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {

    // QUERY NATIVE
    // LISTAR SUCURSALES OPERATIVAS
    @Query(value = "SELECT * FROM SUCURSALES " +
                    "WHERE activa = true " +
                    "ORDER BY nombre ASC",
            nativeQuery = true)
    List<Sucursal> obtenerSucursalesOperativas();
}