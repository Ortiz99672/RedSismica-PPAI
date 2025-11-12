package ppai.redsismica.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import ppai.redsismica.dto.NotificacionDTO;

@Component
public class InterfazNotificaciones implements IObservador {

    private String idSismografo;
    private String estado;
    private LocalDate fecha;
    private Map<String,String> motivos;

    public String getIdSismografo() {
        return idSismografo;
    }
    public void setIdSismografo(String idSismografo) {
        this.idSismografo = idSismografo;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public Map<String, String> getMotivos() {
        return motivos;
    }
    public void setMotivos(Map<String, String> motivos) {
        this.motivos = motivos;
    }

    @Override
    public void actualizar(NotificacionDTO notificacion) {
        System.out.println("InterfazNotificaciones: Recibida actualización para enviar correo.");

        // Obtiene la lista de mails del DTO
        List<String> destinatarios = notificacion.getDestinatarios();

        for (String destinatario : destinatarios) {
            enviarMail(destinatario, notificacion);
        }
    }

    public void enviarMail(String destinatario, NotificacionDTO notificacion) {

        String identificadorSismografo = notificacion.getIdentificadorSismografo();
        String nombreEstado = notificacion.getNombreEstado();
        LocalDateTime fechaHora = notificacion.getFechaHora();
        Map<String, String> motivos = notificacion.getMotivos();

        // Construimos el mensaje
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Asunto: Alerta del Sismógrafo ").append(identificadorSismografo).append("\n\n");
        mensaje.append("Estimado/a,\n\n");
        mensaje.append("Se ha detectado un cambio en el estado del sismógrafo.\n\n");
        mensaje.append("Detalles:\n");
        mensaje.append(" - Sismógrafo: ").append(identificadorSismografo).append("\n");
        mensaje.append(" - Nuevo estado: ").append(nombreEstado).append("\n");
        mensaje.append(" - Fecha y hora: ").append(fechaHora).append("\n");
        mensaje.append(" - Motivos:\n");

        if (motivos != null && !motivos.isEmpty()) {
            for (Map.Entry<String, String> entry : motivos.entrySet()) {
                mensaje.append("     • ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        } else {
            mensaje.append("     (Sin motivos registrados)\n");
        }

        mensaje.append("\nSaludos,\nSistema de Monitoreo Sísmico");

        // Por ahora, simulamos el envío
        System.out.println(">>> (STUB) Enviando mail a: " + destinatario);
        System.out.println("------------------------------------------");
        System.out.println(mensaje);
        System.out.println("------------------------------------------");
    }
}
