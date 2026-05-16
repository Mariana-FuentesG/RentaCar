package com.prueba.ms_clientes.runner;

import com.prueba.ms_clientes.model.Cliente;
import com.prueba.ms_clientes.model.Direccion;
import com.prueba.ms_clientes.repository.ClienteRepository;
import com.prueba.ms_clientes.repository.DireccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DireccionRunner implements CommandLineRunner {

    @Autowired
    DireccionRepository direccionRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Override
    public void run(String... args) throws Exception {

        Cliente cliente1 =
                clienteRepository.findById(1).orElse(null);

        Cliente cliente2 =
                clienteRepository.findById(2).orElse(null);

        Cliente cliente3 =
                clienteRepository.findById(3).orElse(null);

        Cliente cliente4 =
                clienteRepository.findById(4).orElse(null);

        if(cliente1 != null && !direccionRepository.existsById(1)){
            direccionRepository.save(new Direccion(null, "Los Carrera", 123, "Puente Alto", "Santiago", 8820000, true, LocalDate.now(), cliente1));
        }

        if(cliente2 != null && !direccionRepository.existsById(2)){
            direccionRepository.save(new Direccion(null, "Santa Rosa", 456, "La Florida", "Santiago", 8830000, true, LocalDate.now(), cliente2));
        }

        if(cliente3 != null && !direccionRepository.existsById(3)){
            direccionRepository.save(new Direccion(null, "Las Palmas", 789, "Maipu", "Santiago", 9250000, true, LocalDate.now(), cliente3));
        }
        if(cliente4 != null && !direccionRepository.existsById(4)){
            direccionRepository.save(new Direccion(null, "Vicuña Mackenna", 321, "San Joaquin", "Santiago", 8940000, true, LocalDate.now(), cliente4));
        }
        System.out.println(
                "✅ Direcciones cargadas correctamente"
        );
    }
}