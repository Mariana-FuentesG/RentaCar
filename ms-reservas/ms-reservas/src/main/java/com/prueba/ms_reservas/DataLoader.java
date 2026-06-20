package com.prueba.ms_reservas;

import com.prueba.ms_reservas.model.EstadoReserva;
import com.prueba.ms_reservas.model.Reserva;
import com.prueba.ms_reservas.repository.EstadoReservaRepository;
import com.prueba.ms_reservas.repository.ReservaRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EstadoReservaRepository estadoReservaRepository;

    @Override
    public void run(String... args){
        Faker faker = new Faker();
        Random random = new Random();

        // FLYWAY ya insertó los 5 estados y 2 reservas
        // DATAFAKER agrega reservas adicionales con datos inventados
        List<EstadoReserva> estados = estadoReservaRepository.findAll();

        if (estados.isEmpty()) {
            System.out.println("⚠️ No hay estados de reserva disponibles. Verificar Flyway.");
            return;
        }

        // GENERAR 10 RESERVAS ADICIONALES CON FAKER
        for (int i = 0; i < 10; i++) {
            int diasReserva = faker.number().numberBetween(1, 15);
            LocalDate fechaInicio = LocalDate.now().plusDays(
                    faker.number().numberBetween(1, 30));
            LocalDate fechaTermino = fechaInicio.plusDays(diasReserva);
            LocalDate fechaReserva = LocalDate.now();

            Reserva reserva = new Reserva();
            reserva.setClienteId(faker.number().numberBetween(1, 10));
            reserva.setVehiculoId(faker.number().numberBetween(1, 10));
            reserva.setMontoReserva(faker.number().randomDouble(2, 50000, 500000));
            reserva.setCantidadDias(diasReserva);
            reserva.setPagada(faker.bool().bool());
            reserva.setFechaInicio(fechaInicio);
            reserva.setFechaTermino(fechaTermino);
            reserva.setFechaReserva(fechaReserva);
            reserva.setObservacion(faker.lorem().sentence());
            reserva.setEstadoReserva(
                    estados.get(random.nextInt(estados.size())));

            reservaRepository.save(reserva);
        }

        System.out.println("✅ DataLoader: 10 reservas adicionales generadas con Faker.");
    }
}