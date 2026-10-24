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

insert into reserva (cliente, habitacion, check_in, check_out, huespedes, monto, monto_pagado, estado) values ('Ana Torres', '201', '2026-10-21', '2026-10-25', 4, 1280.00, 640.00, 'Confirmada');
insert into reserva (cliente, habitacion, check_in, check_out, huespedes, monto, monto_pagado, estado) values ('Carlos Ramírez', '102', '2026-11-05', '2026-11-08', 2, 600.00, 0.00, 'Pendiente');
insert into reserva (cliente, habitacion, check_in, check_out, huespedes, monto, monto_pagado, estado) values ('Luis Gómez', '202', '2026-11-10', '2026-11-12', 2, 900.00, 900.00, 'Confirmada');

insert into pago (id_reserva, cliente, habitacion, monto, metodo_pago, fecha) values (1, 'Ana Torres', '201', 640.00, 'Tarjeta', '2026-10-21');
insert into pago (id_reserva, cliente, habitacion, monto, metodo_pago, fecha) values (3, 'Luis Gómez', '202', 450.00, 'Efectivo', '2026-10-22');
insert into pago (id_reserva, cliente, habitacion, monto, metodo_pago, fecha) values (3, 'Luis Gómez', '202', 450.00, 'Efectivo', '2026-10-23');