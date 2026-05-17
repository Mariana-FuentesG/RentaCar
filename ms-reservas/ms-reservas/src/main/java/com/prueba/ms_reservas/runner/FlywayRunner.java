package com.prueba.ms_reservas.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FlywayRunner
        implements CommandLineRunner {

    @Override
    public void run(String... args){

        System.out.println(
                "✅ Flyway ejecutado correctamente en ms-reservas, datos iniciales agregados con exíto");
    }
}