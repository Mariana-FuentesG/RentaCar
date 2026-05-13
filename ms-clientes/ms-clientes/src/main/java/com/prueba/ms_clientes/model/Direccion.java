package com.prueba.ms_clientes.model;


import jakarta.persistence.*;

@Entity
@Table(name = "DIRECCIONES")
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String Ciudad;

    @Column(nullable = false)
    private String direccion ;

    @Column(nullable = false)
    private Integer numeroCasa;

    @Column(nullable = false)
    private String comuna;

    @Column(nullable = false)
    private Double codigoPostal;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public Direccion() {
    }
}
