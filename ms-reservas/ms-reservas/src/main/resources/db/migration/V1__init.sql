CREATE TABLE IF NOT EXISTS estado_reserva(
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_estado VARCHAR(50) NOT NULL UNIQUE,
    prioridad INT NOT NULL,
    activo BOOLEAN NOT NULL,
    fecha_creacion DATE NOT NULL,
    dias_limite_pago INT NOT NULL
    );

CREATE TABLE IF NOT EXISTS reservas(
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    vehiculo_id INT NOT NULL,
    monto_reserva DOUBLE NOT NULL,
    cantidad_dias INT NOT NULL,
    pagada BOOLEAN NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_termino DATE NOT NULL,
    fecha_reserva DATE NOT NULL,
    observacion VARCHAR(200) NOT NULL,
    estado_reserva_id INT NOT NULL,
    CONSTRAINT fk_estado_reserva
    FOREIGN KEY (estado_reserva_id)
    REFERENCES estado_reserva(id)
    );