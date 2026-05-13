package com.prueba.ms_clientes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "CLIENTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombreCompleto;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private Integer telefono;

    @Column(nullable = false)
    private Boolean activo;

    @Column(nullable = false)
    private LocalDate fechaRegistro;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Direccion> direcciones;
}
