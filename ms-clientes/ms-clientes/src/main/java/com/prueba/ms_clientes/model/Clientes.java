package com.prueba.ms_clientes.model;

import java.time.LocalDate;

public class Clientes {
    private Integer id;
    private String rutCliente;
    private String nombre;
    @Email
    private String email;
    private Integer telefono;
    private Boolean activo;
    private LocalDate fechaRegistro;
}
