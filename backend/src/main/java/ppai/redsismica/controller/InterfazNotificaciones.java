package ppai.redsismica.controller;

import java.time.LocalDate;
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
    public void actualizar(NotificacionDTO notificacion) { // ¡NUEVA FIRMA!
        System.out.println("InterfazNotificaciones: Recibida actualizacion para enviar correo.");
        // Obtiene la lista de mails del DTO
        List<String> destinatarios = notificacion.getDestinatarios(); 

        for (String destinatario : destinatarios) {
            System.out.println(">>> (STUB) Enviando mail a: " + destinatario);
            enviarMail(notificacion);
        }
    }

    public void enviarMail(NotificacionDTO notificacion) {

        // Logica para construir y enviar el correo electronico.

        // FALTA ESTOOOOOO

        System.out.println("    Sismografo: " + notificacion.getIdentificadorSismografo());
        System.out.println("    Nuevo Estado: " + notificacion.getNombreEstado());
        System.out.println("    Fecha y Hora: " + notificacion.getFechaHora());
        System.out.println("    Motivos: " + notificacion.getMotivos());
    }
}
