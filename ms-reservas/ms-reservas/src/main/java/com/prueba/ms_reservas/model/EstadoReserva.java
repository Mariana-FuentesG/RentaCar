package com.prueba.ms_reservas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "estado_reserva")

public class EstadoReserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(nullable = false)
    private Integer prioridad;

    @Column(nullable = false)
    private Boolean activo;

    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @Column(nullable = false)
    private Integer diasLimitePago;

    @OneToMany(mappedBy = "estadoReserva")
    private List<Reserva> reservas;

}
