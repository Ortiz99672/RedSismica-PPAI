package ppai.redsismica.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO que agrupa toda la información de la notificación
 * que se debe enviar por mail y publicar en monitores.
 * Se devuelve al frontend para mostrar la "vista previa".
 */
public class NotificacionDTO {

    // "identificación del sismógrafo"
    private String identificadorSismografo;

    // "nombre del estado Fuera de Servicio"
    private String nombreEstado;

    // "fecha y hora de registro del nuevo estado"
    private LocalDateTime fechaHora;

    // "motivos y comentarios asociados al cambio"
    private Map<String, String> motivos;

    // "mails de los empleados responsables"
    private List<String> destinatarios;

    public NotificacionDTO(String identificadorSismografo, String nombreEstado,
                           LocalDateTime fechaHora, Map<String, String> motivos,
                           List<String> destinatarios) {
        this.identificadorSismografo = identificadorSismografo;
        this.nombreEstado = nombreEstado;
        this.fechaHora = fechaHora;
        this.motivos = motivos;
        this.destinatarios = destinatarios;
    }

    // --- Getters y Setters ---

    public String getIdentificadorSismografo() {
        return identificadorSismografo;
    }

    public void setIdentificadorSismografo(String identificadorSismografo) {
        this.identificadorSismografo = identificadorSismografo;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Map<String, String> getMotivos() {
        return motivos;
    }

    public void setMotivos(Map<String, String> motivos) {
        this.motivos = motivos;
    }

    public List<String> getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(List<String> destinatarios) {
        this.destinatarios = destinatarios;
    }
}