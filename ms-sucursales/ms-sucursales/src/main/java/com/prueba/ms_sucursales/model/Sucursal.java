package com.prueba.ms_sucursales.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "SUCURSAL")
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String direccion;

    @Column(nullable = false, length = 15)
    private String telefono;

    @Column(nullable = false, length = 50)
    private String ciudad;

    @Column(nullable = false)
    private Boolean activa;

    @Column(nullable = false)
    private Integer cantidadVehiculos;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;
}
