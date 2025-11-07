-- 1. Crear Roles
INSERT INTO rol (nombre, descripcion) VALUES
('Responsable de Inspeccion', 'Rol para el empleado que cierra inspecciones.'),
('Responsable de Reparacion', 'Rol para el empleado que recibe notificaciones de reparación.');

-- 2. Crear Empleados
INSERT INTO empleado (mail, apellido, nombre, telefono, rol_nombre) VALUES
('ri@empresa.com', 'Inspección', 'Juan', '123456', 'Responsable de Inspeccion'),
('reparador@empresa.com', 'Reparación', 'Ana', '789012', 'Responsable de Reparacion');

-- 3. Crear Usuario (para el Responsable de Inspección)
INSERT INTO usuario (nombre_usuario, contrasena, empleado_mail) VALUES
('ri_user', '1234', 'ri@empresa.com');

-- 4. Crear una SESIÓN ACTIVA (MUY IMPORTANTE)
-- La fecha_hora_hasta es NULL, lo que significa que está activa.
INSERT INTO sesion (usuario_nombre_usuario, fecha_hora_desde, fecha_hora_hasta) VALUES
('ri_user', '2025-11-06T09:00:00', NULL);

-- 5. Crear Estados
INSERT INTO estado (nombre_estado, ambito) VALUES
('Completamente Realizada', 'OrdenInspeccion'),
('Cerrada', 'OrdenInspeccion'),
('Activo', 'Sismografo'),
('Fuera de Servicio', 'Sismografo');

-- 6. Crear Estación Sismológica
INSERT INTO estacion_sismologica (codigo_estacion, nombre) VALUES
('EST-A', 'Estación Alfa'),
('EST-B', 'Estación Beta'),
('EST-C', 'Estación Gamma');

-- 7. Crear Sismógrafo (y asociarlo a la estación)
INSERT INTO sismografo (nro_serie, identificador_sismografo, estacion_sismologica_codigo) VALUES
('SISMO-001', 'Sismógrafo A-1', 'EST-A'),
('SISMO-002', 'Sismógrafo A-2', 'EST-B'),
('SISMO-003', 'Sismógrafo A-3', 'EST-C');


-- 8. Crear un Cambio de Estado (el estado actual del sismógrafo)
-- Es "Activo" y no tiene fecha_hora_fin (es el actual)
INSERT INTO cambio_estado (fecha_hora_inicio, estado_nombre, empleado_mail, sismografo_nro_serie) VALUES
('2025-01-01T10:00:00', 'Activo', 'ri@empresa.com', 'SISMO-001');

-- 9. Crear Órdenes de Inspección
-- Esta es la orden que VAMOS a cerrar.
-- Es del empleado 'ri@empresa.com' y está 'Completamente Realizada'.
INSERT INTO orden_inspeccion (nro_orden, fecha_hora_inicio, fecha_hora_finalizacion, estado_nombre_estado, empleado_mail, estacion_sismologica_codigo) VALUES
(101, '2025-11-01T10:00:00', '2025-11-05T14:30:00', 'Completamente Realizada', 'ri@empresa.com', 'EST-A'),
(102, '2025-11-02T10:00:00', '2025-11-04T12:00:00', 'Cerrada', 'ri@empresa.com', 'EST-A'),
(103, '2025-11-03T11:00:00', '2025-11-05T18:30:00', 'Completamente Realizada', 'ri@empresa.com', 'EST-B'),
(104, '2025-11-04T09:30:00', '2025-11-05T10:30:00', 'Completamente Realizada', 'ri@empresa.com', 'EST-C');

-- 10. Crear Motivos Tipo (para poner "Fuera de Servicio")
INSERT INTO motivo_tipo (descripcion) VALUES
('Falla de hardware'),
('Error de software'),
('Mantenimiento programado');