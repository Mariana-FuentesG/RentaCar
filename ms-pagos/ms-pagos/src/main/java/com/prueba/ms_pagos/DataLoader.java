package com.prueba.ms_pagos;

import com.prueba.ms_pagos.model.Pago;
import com.prueba.ms_pagos.repository.PagoRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Locale;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PagoRepository pagoRepository;

    @Override
    public void run(String... args) {
        Faker faker = new Faker(new Locale("es"));

        // FLYWAY YA INSERTÓ LOS DATOS INICIALES
        // DATAFAKER AGREGA PAGOS ADICIONALES CON DATOS INVENTADOS
        for (int i = 0; i < 10; i++) {
            Pago pago = new Pago();
            pago.setReservaId(faker.number().numberBetween(1, 10));
            pago.setMonto(faker.number().randomDouble(2, 50000, 500000));
            pago.setPagado(faker.bool().bool());
            pago.setFechaPago(LocalDate.now().minusDays(
                    faker.number().numberBetween(0, 30)));
            pago.setMetodoPago(faker.options().option(
                    "Tarjeta de Crédito",
                    "Transferencia Bancaria",
                    "Efectivo",
                    "Débito Automático"
            ));
            pago.setCantCuotas(faker.number().numberBetween(1, 12));

            pagoRepository.save(pago);
        }

        System.out.println("✅ DataLoader: 10 pagos adicionales generados con Faker.");
    }
}