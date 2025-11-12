package ppai.redsismica.controller;

import ppai.redsismica.dto.NotificacionDTO;

/**
 * Interfaz del Patrón Observer (Actualizada para pasar solo el DTO).
 * Define el contrato para que las clases (Observadores)
 * reciban notificaciones (actualizaciones) del Sujeto.
 */
public interface IObservador {
    /**
     * Método llamado por el Sujeto para notificar un cambio de estado.
     * @param notificacion DTO con los datos del sismógrafo y la lista de mails.
     */
    void actualizar(NotificacionDTO notificacion);
}