package com.prueba.ms_reservas.repository;

import com.prueba.ms_reservas.model.Reserva;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    @Query("""
            SELECT r
            FROM Reserva r
            WHERE r.fechaInicio >= :fecha
            ORDER BY r.fechaInicio DESC
            """)
    List<Reserva> buscarReservasDesdeFecha(
            @Param("fecha") LocalDate fecha);
}
