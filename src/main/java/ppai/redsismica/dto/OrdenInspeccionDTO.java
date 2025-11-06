package ppai.redsismica.dto;

import java.time.LocalDateTime;

/**
 * DTO que representa los datos de la orden
 * que se muestran en la pantalla (paso 1.2.6)
 */
public class OrdenInspeccionDTO {

    private Integer nroOrden;
    private LocalDateTime fechaHoraFinalizacion; // 1.2.6.5
    private String nombreEstacion; // 1.2.6.1
    private String idSismografo; // 1.2.6.3.2
    private EstadoDTO estado;

    public OrdenInspeccionDTO(Integer nroOrden, LocalDateTime fechaHoraFinalizacion,
                              String nombreEstacion, String idSismografo, EstadoDTO estado) {
        this.nroOrden = nroOrden;
        this.fechaHoraFinalizacion = fechaHoraFinalizacion;
        this.nombreEstacion = nombreEstacion;
        this.idSismografo = idSismografo;
        this.estado = estado;
    }

    // Getters y Setters
    public Integer getNroOrden() { return nroOrden; }
    public void setNroOrden(Integer nroOrden) { this.nroOrden = nroOrden; }
    public LocalDateTime getFechaHoraFinalizacion() { return fechaHoraFinalizacion; }
    public void setFechaHoraFinalizacion(LocalDateTime fechaHoraFinalizacion) { this.fechaHoraFinalizacion = fechaHoraFinalizacion; }
    public String getNombreEstacion() { return nombreEstacion; }
    public void setNombreEstacion(String nombreEstacion) { this.nombreEstacion = nombreEstacion; }
    public String getIdSismografo() { return idSismografo; }
    public void setIdSismografo(String idSismografo) { this.idSismografo = idSismografo; }
    public EstadoDTO getEstado() { return estado; }
    public void setEstado(EstadoDTO estado) { this.estado = estado; }
}
