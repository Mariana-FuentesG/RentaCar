package com.prueba.ms_sucursales.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SUCURSALES")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private Boolean activa;

    @Column(nullable = false)
    private Integer cantidadVehiculos;
}
