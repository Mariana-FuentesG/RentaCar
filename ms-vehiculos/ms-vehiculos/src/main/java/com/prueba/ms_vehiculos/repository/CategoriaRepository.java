package com.prueba.ms_vehiculos.repository;
import com.prueba.ms_vehiculos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
