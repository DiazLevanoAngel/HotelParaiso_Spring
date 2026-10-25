create table if not exists categoria(
    id          int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre      VARCHAR(100) not null,
    descripcion VARCHAR(500),
    capacidad   int not null,
    precio      double precision not null,
    estado      VARCHAR(20) not null,
    activo      boolean default true not null
    );

create table if not exists cliente(
    id        int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre    VARCHAR(200) not null,
    documento VARCHAR(30) not null,
    telefono  VARCHAR(30),
    correo    VARCHAR(150),
    activo    boolean default true not null
    );

create table if not exists habitacion(
    id           int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    numero       VARCHAR(20) not null,
    descripcion  VARCHAR(500),
    estado       VARCHAR(30) not null,
    id_categoria int not null,
    activo       boolean default true not null,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id)
    );

create table if not exists reserva(
    id           int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cliente      VARCHAR(200) not null,
    habitacion   VARCHAR(20) not null,
    check_in     date not null,
    check_out    date not null,
    huespedes    int,
    monto        double precision,
    monto_pagado double precision default 0,
    estado       VARCHAR(20) default 'Pendiente',
    activo       boolean default true not null
    );

create table if not exists pago(
    id          int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_reserva  int not null,
    cliente     VARCHAR(200),
    habitacion  VARCHAR(20),
    monto       double precision not null,
    metodo_pago VARCHAR(30),
    fecha       date,
    activo       boolean default true not null,
    FOREIGN KEY (id_reserva) REFERENCES reserva(id)
    );