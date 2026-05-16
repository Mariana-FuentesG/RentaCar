package com.prueba.ms_clientes.runner;

import com.prueba.ms_clientes.model.Cliente;
import com.prueba.ms_clientes.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ClienteRunner implements CommandLineRunner {

    @Autowired
    ClienteRepository clienteRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!clienteRepository.existsById(1)){
            clienteRepository.save(new Cliente(null,123789237,"Juan Gomez","juangomez@gmail.com","956743278",true,LocalDate.now(),null));
        }
        if (!clienteRepository.existsById(2)){
            clienteRepository.save(new Cliente(null,145768308,"Catalina Muñoz","cata.muñoz@gmail.com","987543219",true, LocalDate.now(),null));
        }
        if (!clienteRepository.existsById(3)){
            clienteRepository.save(new Cliente(null,200633871,"Dario Gonzalez","dariogonzalez@gmail.com","986023418",true, LocalDate.now(),null));
        }
        if (!clienteRepository.existsById(4)){
            clienteRepository.save(new Cliente(null,197654293,"Camila Perez","camila.perezz@gmail.com","900967438",true, LocalDate.now(),null));
        }
        System.out.println(
                "✅ Datos iniciales de Persona fueron cargados correctamente");
    }
}
