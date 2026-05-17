INSERT INTO estado_reserva
(nombre,
 prioridad,
 activo,
 fecha_creacion,
 dias_limite_pago)
VALUES
('Pendiente', 1, true, CURRENT_DATE, 3),    ('Confirmada', 2, true, CURRENT_DATE, 1),
('Pagada', 3, true, CURRENT_DATE, 0),
('Cancelada', 4, true, CURRENT_DATE, 0),
('Finalizada', 5, true, CURRENT_DATE, 0);