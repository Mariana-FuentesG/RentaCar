INSERT INTO reservas
(cliente_id,
 vehiculo_id,
 monto_reserva,
 cantidad_dias,
 pagada,
 fecha_inicio,
 fecha_termino,
 fecha_reserva,
 observacion,
 estado_reserva_id)
VALUES

    (1, 1, 150000, 3, true,
     CURRENT_DATE,
     CURRENT_DATE + 3,
     CURRENT_DATE,
     'Reserva inicial',
     3),

    (2, 2, 300000, 5, false,
     CURRENT_DATE,
     CURRENT_DATE + 5,
     CURRENT_DATE,
     'Reserva pendiente',
     1);