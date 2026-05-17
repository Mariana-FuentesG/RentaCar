package com.prueba.ms_empleados.runner;

import com.prueba.ms_empleados.model.Empleado;
import com.prueba.ms_empleados.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class EmpleadoRunner implements CommandLineRunner {

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Override
    public void run(String... args) throws Exception {

        // EMPLEADO 1
        if(!empleadoRepository.existsById(1)) {
            Empleado empleado1 = new Empleado();
            empleado1.setNombreCompleto("Juan Perez");
            empleado1.setEmail("juan.perez@gmail.com");
            empleado1.setTelefono("987654321");
            empleado1.setCargo("Administrador");
            empleado1.setSueldo(1200000.0);
            empleado1.setActivo(true);
            empleado1.setFechaContratacion(LocalDate.of(2024, 3, 10));
            empleadoRepository.save(empleado1);
        }
        // EMPLEADO 2
        if(!empleadoRepository.existsById(2)) {
            Empleado empleado2 = new Empleado();
            empleado2.setNombreCompleto("Maria Gonzalez");
            empleado2.setEmail("maria.gonzalez@gmail.com");
            empleado2.setTelefono("912345678");
            empleado2.setCargo("Ejecutiva Comercial");
            empleado2.setSueldo(950000.0);
            empleado2.setActivo(true);
            empleado2.setFechaContratacion(LocalDate.of(2023, 8, 15));
            empleadoRepository.save(empleado2);
        }
        // EMPLEADO 3
        if(!empleadoRepository.existsById(3)) {
            Empleado empleado3 = new Empleado();
            empleado3.setNombreCompleto("Carlos Rojas");
            empleado3.setEmail("carlos.rojas@gmail.com");
            empleado3.setTelefono("923456789");
            empleado3.setCargo("Soporte Tecnico");
            empleado3.setSueldo(800000.0);
            empleado3.setActivo(false);
            empleado3.setFechaContratacion(LocalDate.of(2022, 1, 20));
            empleadoRepository.save(empleado3);
        }
        System.out.println(
                "✅ Datos iniciales de empleados cargados correctamente");
    }
}

