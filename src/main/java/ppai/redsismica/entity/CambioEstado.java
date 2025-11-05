package ppai.redsismica.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CambioEstado {

    @Id
    private LocalDateTime fechaHoraInicio;

    private LocalDateTime fechaHoraFin;

    @OneToMany(cascade = CascadeType.ALL)
    private String motivoFueraServicio;

    @ManyToOne
    @JoinColumn(name = "estado_nombre")
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "empleado_mail")
    private Empleado empleado;

    // --- Constructores ---
    public CambioEstado() {
    }

    public CambioEstado(LocalDateTime fechaHoraInicio, Estado estado, Empleado empleado) {
        this.fechaHoraInicio = fechaHoraInicio;
        this.estado = estado;
        this.empleado = empleado;
    }

    // --- Métodos del diagrama ---
    public boolean esEstadoActual() {
        return this.fechaHoraFin == null;
    }

    // --- Getters y Setters ---
    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public String getMotivoFueraServicio() {
        return motivoFueraServicio;
    }

    public void setMotivoFueraServicio(String motivoFueraServicio) {
        this.motivoFueraServicio = motivoFueraServicio;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
}
