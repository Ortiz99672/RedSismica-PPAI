package ppai.redsismica.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import ppai.redsismica.dto.NotificacionDTO;

/**
 * Entidad que representa una Estación Sismológica.
 * El campo `codigoEstacion` es la clave primaria.
 */
@Entity
public class EstacionSismologica {

    @Id
    private String codigoEstacion; // Clave primaria

    private String documentoCertificacionAdd;
    private LocalDate fechaSolicitudCertificacion;
    private Double latitud;
    private Double longitud;
    private String nombre;
    private String nroCertificacionAdquisicion;

    @OneToOne (mappedBy = "estacionSismologica")
    private Sismografo sismografo; // Relación con Sismografo

    // --- Constructores ---
    public EstacionSismologica() {
    }

    public EstacionSismologica(String codigoEstacion, String documentoCertificacionAdd, LocalDate fechaSolicitudCertificacion, Double latitud, Double longitud, String nombre, String nroCertificacionAdquisicion) {
        this.codigoEstacion = codigoEstacion;
        this.documentoCertificacionAdd = documentoCertificacionAdd;
        this.fechaSolicitudCertificacion = fechaSolicitudCertificacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.nroCertificacionAdquisicion = nroCertificacionAdquisicion;
    }

    // --- Métodos de Negocio ---

    /**
     * Delega al Sismógrafo asociado la responsabilidad de cambiar su estado
     * a Fuera de Servicio, iniciando un nuevo registro de CambioEstado.
     * @param estadoFS El estado "Fuera de Servicio" para el sismógrafo.
     * @param fechaYHoraActual Fecha y hora del cambio.
     * @param empleadoRI El Empleado (RI) que realiza la acción.
     * @param todosLosMotivos Lista completa de MotivoTipo.
     * @param motivosSeleccionadosConComentario Map<MotivoTipo, Comentario> seleccionados.
     */
    /**
     * 7.1.12.1.2: Implementación de "setSismografoFueraDeServicio"
     * Delega la tarea al Sismógrafo asociado (Fragmento 1).
     * @return El DTO de notificación retornado por Sismografo.
     */
    public NotificacionDTO setSismografoFueraDeServicio(Estado estadoFS, LocalDateTime fechaYHoraActual, Empleado empleadoRI,
                                                        List<MotivoTipo> todosLosMotivos, Map<String, String> motivosSeleccionadosConComentario,
                                                        List<String> mailsResponsablesReparacion) { // <--- Nuevo parámetro
        
        System.out.println("EstacionSismologica: Ejecutando 7.1.12.1.2 setSismografoFueraDeServicio()...");
        if (this.sismografo != null) {
            // 7.1.12.1.3: Llama a setSismografoFueraDeServicio del Sismógrafo
            return this.sismografo.setSismografoFueraDeServicio(
                estadoFS,
                fechaYHoraActual,
                empleadoRI,
                todosLosMotivos,
                motivosSeleccionadosConComentario,
                mailsResponsablesReparacion // <--- Pasa el nuevo parámetro
            );
        } else {
            System.err.println("EstacionSismologica: Advertencia: No hay Sismografo asociado para cambiar de estado.");
            return null;
        }
    }

    // --- Getters y Setters ---
    public String getCodigoEstacion() {
        return codigoEstacion;
    }
    public void setCodigoEstacion(String codigoEstacion) {
        this.codigoEstacion = codigoEstacion;
    }
    public Sismografo getSismografo() {
        return sismografo;
    }
    public void setSismografo(Sismografo sismografo) {
        this.sismografo = sismografo;
    }
    public Double getLatitud() {
        return latitud;
    }
    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }
    public Double getLongitud() {
        return longitud;
    }
    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getNroCertificacionAdquisicion() {
        return nroCertificacionAdquisicion;
    }
    public void setNroCertificacionAdquisicion(String nroCertificacionAdquisicion) {
        this.nroCertificacionAdquisicion = nroCertificacionAdquisicion;
    }
    public LocalDate getFechaSolicitudCertificacion() {
        return fechaSolicitudCertificacion;
    }
    public void setFechaSolicitudCertificacion(LocalDate fechaSolicitudCertificacion) {
        this.fechaSolicitudCertificacion = fechaSolicitudCertificacion;
    }
    public String getDocumentoCertificacionAdd() {
        return documentoCertificacionAdd;
    }
    public void setDocumentoCertificacionAdd(String documentoCertificacionAdd) {
        this.documentoCertificacionAdd = documentoCertificacionAdd;
    }

}