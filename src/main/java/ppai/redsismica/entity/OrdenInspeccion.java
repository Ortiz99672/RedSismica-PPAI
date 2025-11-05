package ppai.redsismica.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class OrdenInspeccion {

    @Id
    private Integer nroOrden;

    private LocalDateTime fechaHoraCierre;
    private LocalDateTime fechaHoraFinalizacion;
    private LocalDateTime fechaHoraInicio;
    private String observacionCierre;

    @ManyToOne
    @JoinColumn(name = "estado_nombreEstado")
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "empleado_mail")
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "estacion_sismologica_codigo")
    private EstacionSismologica estacionSismologica;

    // --- Constructores ---
    public OrdenInspeccion() {
    }

    public OrdenInspeccion(Integer nroOrden, LocalDateTime fechaHoraInicio, Estado estado, Empleado empleado, EstacionSismologica estacionSismologica) {
        this.nroOrden = nroOrden;
        this.fechaHoraInicio = fechaHoraInicio;
        this.estado = estado;
        this.empleado = empleado;
        this.estacionSismologica = estacionSismologica;
    }

    // --- Métodos del diagrama ---
    public boolean esDeEmpleado(Empleado empleado) {
        return this.empleado != null && this.empleado.getMail().equals(empleado.getMail());
    }

    public boolean sosEstadoCompletamenteRealizado() {
        return this.estado != null && this.estado.esCompletamenteRealizada();
    }

    public void mostrarDatosOrden() {
        // Lógica a implementar
    }

    public Integer getNroOrden() {
        return this.nroOrden;
    }

    public LocalDateTime getFechaFinalizacion() {
        return this.fechaHoraFinalizacion;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setFechaHoraCierre(LocalDateTime fechaHoraCierre) {
        this.fechaHoraCierre = fechaHoraCierre;
    }

    public void cerrar() {
        // Lógica a implementar
    }

    public void enviarSismografoParaReparacion() {
        // Lógica a implementar
    }

    // --- Getters y Setters adicionales ---
    public void setNroOrden(Integer nroOrden) {
        this.nroOrden = nroOrden;
    }

    public LocalDateTime getFechaHoraCierre() {
        return fechaHoraCierre;
    }

    public LocalDateTime getFechaHoraFinalizacion() {
        return fechaHoraFinalizacion;
    }

    public void setFechaHoraFinalizacion(LocalDateTime fechaHoraFinalizacion) {
        this.fechaHoraFinalizacion = fechaHoraFinalizacion;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public String getObservacionCierre() {
        return observacionCierre;
    }

    public void setObservacionCierre(String observacionCierre) {
        this.observacionCierre = observacionCierre;
    }

    public Estado getEstado() {
        return estado;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public EstacionSismologica getEstacionSismologica() {
        return estacionSismologica;
    }

    public void setEstacionSismologica(EstacionSismologica estacionSismologica) {
        this.estacionSismologica = estacionSismologica;
    }
}
