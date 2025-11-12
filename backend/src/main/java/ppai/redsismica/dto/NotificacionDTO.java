package ppai.redsismica.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object (DTO) para la notificación de cambio de estado de Sismógrafo.
 * Contiene los datos del evento y la lista de destinatarios para el envío de mails.
 */
public class NotificacionDTO {

    private String identificadorSismografo;
    private String nombreEstado;
    private LocalDateTime fechaHora;
    private Map<String, String> motivos;
    // NUEVO CAMPO: Lista de correos a los que se enviará la notificación
    private List<String> destinatarios; 

    // Constructor completo
    public NotificacionDTO(String identificadorSismografo, String nombreEstado, LocalDateTime fechaHora, Map<String, String> motivos, List<String> destinatarios) {
        this.identificadorSismografo = identificadorSismografo;
        this.nombreEstado = nombreEstado;
        this.fechaHora = fechaHora;
        this.motivos = motivos;
        this.destinatarios = destinatarios; 
    }

    // --- Getters ---
    public String getIdentificadorSismografo() { return identificadorSismografo; }
    public String getNombreEstado() { return nombreEstado; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public Map<String, String> getMotivos() { return motivos; }
    public List<String> getDestinatarios() { return destinatarios; }
    
    // ... (Se pueden omitir Setters si el DTO es inmutable)
}