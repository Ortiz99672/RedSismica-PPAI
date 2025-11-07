-- Borramos todo en orden inverso para evitar errores de FK
DROP TABLE IF EXISTS cambio_estado_motivos CASCADE;
DROP TABLE IF EXISTS motivo_fuera_de_servicio CASCADE;
DROP TABLE IF EXISTS cambio_estado CASCADE;
DROP TABLE IF EXISTS orden_inspeccion CASCADE;
DROP TABLE IF EXISTS sismografo CASCADE;
DROP TABLE IF EXISTS estacion_sismologica CASCADE;
DROP TABLE IF EXISTS sesion CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS empleado CASCADE;
DROP TABLE IF EXISTS rol CASCADE;
DROP TABLE IF EXISTS estado CASCADE;
DROP TABLE IF EXISTS motivo_tipo CASCADE;

-- Creación de tablas

CREATE TABLE rol (
    nombre VARCHAR(255) PRIMARY KEY,
    descripcion VARCHAR(255)
);

CREATE TABLE empleado (
    mail VARCHAR(255) PRIMARY KEY,
    apellido VARCHAR(255),
    nombre VARCHAR(255),
    telefono VARCHAR(50),
    rol_nombre VARCHAR(255),
    FOREIGN KEY (rol_nombre) REFERENCES rol(nombre)
);

CREATE TABLE usuario (
    nombre_usuario VARCHAR(50) PRIMARY KEY,
    contrasena VARCHAR(255),
    empleado_mail VARCHAR(255) UNIQUE,
    FOREIGN KEY (empleado_mail) REFERENCES empleado(mail)
);

CREATE TABLE sesion (
    usuario_nombre_usuario VARCHAR(50) PRIMARY KEY,
    fecha_hora_desde TIMESTAMP,
    fecha_hora_hasta TIMESTAMP,
    FOREIGN KEY (usuario_nombre_usuario) REFERENCES usuario(nombre_usuario)
);

CREATE TABLE estado (
    nombre_estado VARCHAR(255) PRIMARY KEY,
    ambito VARCHAR(255)
);

CREATE TABLE estacion_sismologica (
    codigo_estacion VARCHAR(255) PRIMARY KEY,
    documento_certificacion_add VARCHAR(255),
    fecha_solicitud_certificacion DATE,
    latitud DOUBLE PRECISION,
    longitud DOUBLE PRECISION,
    nombre VARCHAR(255),
    nro_certificacion_adquisicion VARCHAR(255)
);

CREATE TABLE sismografo (
    nro_serie VARCHAR(255) PRIMARY KEY,
    fecha_adquisicion DATE,
    identificador_sismografo VARCHAR(255),
    estacion_sismologica_codigo VARCHAR(255) UNIQUE,
    FOREIGN KEY (estacion_sismologica_codigo) REFERENCES estacion_sismologica(codigo_estacion)
);

CREATE TABLE orden_inspeccion (
    nro_orden INT PRIMARY KEY,
    fecha_hora_cierre TIMESTAMP,
    fecha_hora_finalizacion TIMESTAMP,
    fecha_hora_inicio TIMESTAMP,
    observacion_cierre TEXT,
    estado_nombre_estado VARCHAR(255),
    empleado_mail VARCHAR(255),
    estacion_sismologica_codigo VARCHAR(255),
    FOREIGN KEY (estado_nombre_estado) REFERENCES estado(nombre_estado),
    FOREIGN KEY (empleado_mail) REFERENCES empleado(mail),
    FOREIGN KEY (estacion_sismologica_codigo) REFERENCES estacion_sismologica(codigo_estacion)
);

CREATE TABLE motivo_tipo (
                             descripcion VARCHAR(255) PRIMARY KEY
);

CREATE TABLE motivo_fuera_de_servicio (
                                          comentario VARCHAR(255) PRIMARY KEY,
                                          motivo_tipo VARCHAR(255),
                                          FOREIGN KEY (motivo_tipo) REFERENCES motivo_tipo(descripcion)
);

CREATE TABLE cambio_estado (
    fecha_hora_inicio TIMESTAMP PRIMARY KEY,
    fecha_hora_fin TIMESTAMP,
    estado_nombre VARCHAR(255),
    empleado_mail VARCHAR(255),
    sismografo_nro_serie VARCHAR(255),
    motivo_fuera_servicio_comentario VARCHAR(255),
    FOREIGN KEY (estado_nombre) REFERENCES estado(nombre_estado),
    FOREIGN KEY (empleado_mail) REFERENCES empleado(mail),
    FOREIGN KEY (sismografo_nro_serie) REFERENCES sismografo(nro_serie),
    FOREIGN KEY (motivo_fuera_servicio_comentario) REFERENCES motivo_fuera_de_servicio(comentario)
);

-- Tabla de unión para la relación @OneToMany entre CambioEstado y MotivoFueraDeServicio
CREATE TABLE cambio_estado_motivos (
    cambio_estado_fecha_hora_inicio TIMESTAMP NOT NULL,
    motivos_fuera_servicio_comentario VARCHAR(255) NOT NULL,
    PRIMARY KEY (cambio_estado_fecha_hora_inicio, motivos_fuera_servicio_comentario),
    FOREIGN KEY (cambio_estado_fecha_hora_inicio) REFERENCES cambio_estado(fecha_hora_inicio),
    FOREIGN KEY (motivos_fuera_servicio_comentario) REFERENCES motivo_fuera_de_servicio(comentario)
);