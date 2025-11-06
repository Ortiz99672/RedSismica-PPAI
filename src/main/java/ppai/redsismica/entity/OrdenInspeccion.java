package ppai.redsismica.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import ppai.redsismica.dto.OrdenInspeccionDTO;
import ppai.redsismica.dto.EstadoDTO;

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
    /**
     * Implementa 1.2.4: esDeEmpleado()
     */
    public boolean esDeEmpleado(Empleado empleado) {
        // Comparamos por el mail (PK de Empleado)
        return this.empleado != null &&
                empleado != null &&
                this.empleado.getMail().equals(empleado.getMail());
    }

    /**
     * Implementa 1.2.5: sosEstadoCompletamenteRealizado()
     */
    public boolean sosEstadoCompletamenteRealizado() {
        // 1.2.5.1: Delega la lógica al estado
        return this.estado != null && this.estado.esCompletamenteRealizada();
    }

    /**
     * Implementa 1.2.6: mostrarDatosOrden()
     * El "mostrar" en el backend se traduce a "recolectar
     * datos y devolver un DTO".
     */
    public OrdenInspeccionDTO mapearADTO() {
        String nombreEstacion = null;
        String idSismografo = null;

        if (this.estacionSismologica != null) {
            // 1.2.6.1: getNombre()
            nombreEstacion = this.estacionSismologica.getNombre();

            // 1.2.6.2: obtenerSismografo()
            Sismografo sismografo = this.estacionSismologica.getSismografo();

            if (sismografo != null) {
                // 1.2.6.3: (Llamada implícita)
                sismografo.sosDeEstacionSismologica(); // (Solo validación)

                // 1.2.6.3.2: getIdentificadorSismografo()
                idSismografo = sismografo.getIdentificadorSismografo();
            }
        }

        // 1.2.6.4: getNroOrden() (es this.nroOrden)
        // 1.2.6.5: getFechaFinalizacion()
        LocalDateTime fechaFin = getFechaFinalizacion();

        EstadoDTO estadoDTO = (this.estado != null) ? this.estado.mapearADTO() : null;

        return new OrdenInspeccionDTO(this.nroOrden, fechaFin, nombreEstacion, idSismografo, estadoDTO);
    }

    public Integer getNroOrden() {
        return this.nroOrden;
    }

    public LocalDateTime getFechaFinalizacion() {
        return this.fechaHoraFinalizacion;
    }

    /**
     * 7.1.10.2: setEstado()
     */
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    /**
     * 7.1.10.1: setFechaHoraCierre()
     */
    public void setFechaHoraCierre(LocalDateTime fechaHoraCierre) {
        this.fechaHoraCierre = fechaHoraCierre;
    }


    /**
     * 7.1.10: Implementación de "cerrar"
     */
    public void cerrar(Estado estadoCerrada, LocalDateTime fechaCierre, String observacion) {
        System.out.println("OrdenInspeccion: Ejecutando 7.1.10 cerrar()...");

        // 7.1.10.1: Llama a su propio setter
        this.setFechaHoraCierre(fechaCierre);

        // 7.1.10.2: Llama a su propio setter
        this.setEstado(estadoCerrada);

        // (Llamada implícita)
        this.setObservacionCierre(observacion);
    }

    public void enviarSismografoParaReparacion() {
        if(this.estacionSismologica != null) {
            this.estacionSismologica.ponerSismografoFueraDeServicio(); // O lógica similar
        }
        System.out.println("OrdenInspeccion: STUB - enviarSismografoParaReparacion()");
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
