package com.prueba.ms_vehiculos;

import com.prueba.ms_vehiculos.model.Categoria;
import com.prueba.ms_vehiculos.model.Vehiculo;
import com.prueba.ms_vehiculos.repository.CategoriaRepository;
import com.prueba.ms_vehiculos.repository.VehiculoRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public void run(String... args) {
        Faker faker = new Faker(new Locale("es"));
        Random random = new Random();

        // OBTENER CATEGORIAS EXISTENTES PARA ASIGNAR A LOS VEHICULOS
        List<Categoria> categorias = categoriaRepository.findAll();

        if (categorias.isEmpty()) {
            System.out.println("⚠️ No hay categorías disponibles. Verificar datos iniciales.");
            return;
        }

        // DATAFAKER AGREGA VEHICULOS ADICIONALES CON DATOS INVENTADOS
        for (int i = 0; i < 10; i++) {
            Vehiculo vehiculo = new Vehiculo();
            vehiculo.setPatente(faker.bothify("????##").toUpperCase());
            vehiculo.setMarca(faker.options().option(
                    "Toyota", "Chevrolet", "Hyundai", "Kia",
                    "Nissan", "Ford", "Suzuki", "Mazda"
            ));
            vehiculo.setModelo(faker.options().option(
                    "Corolla", "Spark", "Tucson", "Sportage",
                    "Sentra", "Explorer", "Swift", "CX-5"
            ));
            vehiculo.setPrecioDiario(faker.number().randomDouble(2, 30000, 150000));
            vehiculo.setAnio(faker.number().numberBetween(2015, 2026));
            vehiculo.setDisponible(faker.bool().bool());
            vehiculo.setFechaIngreso(LocalDate.now().minusDays(
                    faker.number().numberBetween(0, 365)));
            vehiculo.setCategoria(categorias.get(random.nextInt(categorias.size())));

            vehiculoRepository.save(vehiculo);
        }

        System.out.println("✅ DataLoader: 10 vehículos adicionales generados con Faker.");
    }
}