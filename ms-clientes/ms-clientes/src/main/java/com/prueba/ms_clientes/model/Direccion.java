package com.prueba.ms_clientes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "DIRECCIONES")

public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String calle ;

    @Column(nullable = false)
    private Integer numeroCasa;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private Integer codigoPostal;

    @Column(nullable = false)
    private Boolean estado;

    @Column(nullable = false)
    private LocalDate fechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Cliente_Id")
    private Cliente cliente;

}
