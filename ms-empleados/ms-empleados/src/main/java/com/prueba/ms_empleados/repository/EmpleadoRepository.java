package com.prueba.ms_empleados.repository;

import com.prueba.ms_empleados.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    // QUERY NATIVE
    // LISTAR EMPLEADOS ACTIVOS
    // CONTRATADOS EN UN AÑO DETERMINADO
    @Query(value =
            "SELECT * FROM EMPLEADOS " +
                    "WHERE activo = true " +
                    "AND YEAR(fecha_contratacion) = :anio",
            nativeQuery = true)
    List<Empleado> obtenerEmpleadosActivosPorAnio(@Param("anio") Integer anio);
}
