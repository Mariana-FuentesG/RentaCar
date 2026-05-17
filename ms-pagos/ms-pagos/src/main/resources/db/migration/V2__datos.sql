INSERT INTO pagos
(reserva_id,
 monto,
 pagado,
 fecha_pago,
 metodo_pago,
 cant_cuotas)
VALUES
(1, 150000, true, CURRENT_DATE, 'Tarjeta', 1),
(2, 300000, false, CURRENT_DATE, 'Transferencia', 3),
(3, 450000, true, CURRENT_DATE, 'Debito', 1);