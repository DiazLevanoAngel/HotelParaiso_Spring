insert into categoria (nombre, descripcion, capacidad, precio, estado) values ('Individual', 'Habitación para una persona', 1, 120.00, 'Activa');
insert into categoria (nombre, descripcion, capacidad, precio, estado) values ('Doble', 'Habitación con cama matrimonial o dos camas', 2, 200.00, 'Activa');
insert into categoria (nombre, descripcion, capacidad, precio, estado) values ('Familiar', 'Habitación amplia para toda la familia', 4, 320.00, 'Activa');
insert into categoria (nombre, descripcion, capacidad, precio, estado) values ('Suite', 'Suite con sala y vista panorámica', 2, 450.00, 'Activa');

insert into cliente (nombre, documento, telefono, correo) values ('Ana Torres', '45871236', '987654321', 'ana.torres@correo.com');
insert into cliente (nombre, documento, telefono, correo) values ('Carlos Ramírez', '70123456', '956123789', 'carlos.ramirez@correo.com');
insert into cliente (nombre, documento, telefono, correo) values ('Luis Gómez', '41236587', '945812367', 'luis.gomez@correo.com');

insert into habitacion (numero, descripcion, estado, id_categoria) values ('101', 'Primer piso, vista al jardín', 'Disponible', 1);
insert into habitacion (numero, descripcion, estado, id_categoria) values ('102', 'Primer piso, cerca de recepción', 'Disponible', 2);
insert into habitacion (numero, descripcion, estado, id_categoria) values ('201', 'Segundo piso, con balcón', 'Ocupada', 3);
insert into habitacion (numero, descripcion, estado, id_categoria) values ('202', 'Segundo piso, vista a la piscina', 'Disponible', 4);
insert into habitacion (numero, descripcion, estado, id_categoria) values ('301', 'Tercer piso, en mantenimiento', 'Fuera de servicio', 2);

insert into reserva (cliente, habitacion, check_in, check_out, huespedes, monto, monto_pagado, estado, estado_reserva, fecha_creacion) values ('Ana Torres', '201', '2026-10-21', '2026-10-25', 4, 1280.00, 640.00, 'Confirmada', 'Asistio', '2026-10-01');
insert into reserva (cliente, habitacion, check_in, check_out, huespedes, monto, monto_pagado, estado, estado_reserva, fecha_creacion) values ('Carlos Ramírez', '102', '2026-11-05', '2026-11-08', 2, 600.00, 0.00, 'Pendiente', 'Reservada', '2026-10-05');
insert into reserva (cliente, habitacion, check_in, check_out, huespedes, monto, monto_pagado, estado, estado_reserva, fecha_creacion) values ('Luis Gómez', '202', '2026-11-10', '2026-11-12', 2, 900.00, 900.00, 'Confirmada', 'Reservada', '2026-10-12');
insert into reserva (cliente, habitacion, check_in, check_out, huespedes, monto, monto_pagado, estado, estado_reserva, fecha_creacion) values ('Ana Torres', '101', '2026-09-16', '2026-09-19', 1, 360.00, 180.00, 'Confirmada', 'Asistio', '2026-09-15');
insert into reserva (cliente, habitacion, check_in, check_out, huespedes, monto, monto_pagado, estado, estado_reserva, fecha_creacion) values ('Carlos Ramírez', '201', '2026-09-24', '2026-09-27', 4, 960.00, 960.00, 'Confirmada', 'Asistio', '2026-09-22');
insert into reserva (cliente, habitacion, check_in, check_out, huespedes, monto, monto_pagado, estado, estado_reserva, fecha_creacion, fecha_cancelacion) values ('Luis Gómez', '102', '2026-10-08', '2026-10-11', 2, 600.00, 0.00, 'Pendiente', 'Cancelada', '2026-10-05', '2026-10-10');
insert into reserva (cliente, habitacion, check_in, check_out, huespedes, monto, monto_pagado, estado, estado_reserva, fecha_creacion) values ('Ana Torres', '202', '2026-10-17', '2026-10-20', 2, 1350.00, 675.00, 'Confirmada', 'Asistio', '2026-10-12');

insert into pago (id_reserva, cliente, habitacion, monto, metodo_pago, fecha) values (1, 'Ana Torres', '201', 640.00, 'Tarjeta', '2026-10-21');
insert into pago (id_reserva, cliente, habitacion, monto, metodo_pago, fecha) values (3, 'Luis Gómez', '202', 450.00, 'Efectivo', '2026-10-22');
insert into pago (id_reserva, cliente, habitacion, monto, metodo_pago, fecha) values (3, 'Luis Gómez', '202', 450.00, 'Efectivo', '2026-10-23');
insert into pago (id_reserva, cliente, habitacion, monto, metodo_pago, fecha) values (4, 'Ana Torres', '101', 180.00, 'Tarjeta', '2026-09-16');
insert into pago (id_reserva, cliente, habitacion, monto, metodo_pago, fecha) values (5, 'Carlos Ramírez', '201', 960.00, 'Efectivo', '2026-09-24');
insert into pago (id_reserva, cliente, habitacion, monto, metodo_pago, fecha) values (7, 'Ana Torres', '202', 675.00, 'Tarjeta', '2026-10-17');