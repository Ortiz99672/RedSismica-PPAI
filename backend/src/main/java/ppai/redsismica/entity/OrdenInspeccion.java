package ppai.redsismica.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import ppai.redsismica.dto.EstadoDTO;
import ppai.redsismica.dto.OrdenInspeccionDTO;

/**
 * Entidad que representa una Orden de Inspección.
 * El campo `nroOrden` es la clave primaria.
 */
@Entity
public class OrdenInspeccion {

    @Id
    private Integer nroOrden; // Clave primaria

    private LocalDateTime fechaHoraCierre;
    private LocalDateTime fechaHoraFinalizacion;
    private LocalDateTime fechaHoraInicio;
    private String observacionCierre;

    @ManyToOne
    @JoinColumn(name = "estado_nombreEstado")
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "empleado_mail")
    private Empleado empleado; // Responsable de Inspección (RI)

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

    // --- Método de Mapeo ---
    /**
     * Mapea la entidad a un DTO.
     */
    public OrdenInspeccionDTO mapearADTO() {
        
        // 1. Mapear Estado a EstadoDTO
        EstadoDTO estadoDTO = (estado != null) ? estado.mapearADTO() : null; // Se asume que Estado.java tiene mapearADTO()

        // 2. Obtener los campos específicos de EstacionSismologica y Sismografo
        String nombreEstacion = null;
        String idSismografo = null;

        if (estacionSismologica != null) {
            nombreEstacion = estacionSismologica.getNombre(); // getNombre() en EstacionSismologica.java
            
            if (estacionSismologica.getSismografo() != null) {
                // getIdentificadorSismografo() en Sismografo.java (se asume accesible)
                idSismografo = estacionSismologica.getSismografo().getIdentificadorSismografo(); 
            }
        }

        // Se llama al constructor de OrdenInspeccionDTO con 5 argumentos:
        return new OrdenInspeccionDTO(
            this.nroOrden,
            this.fechaHoraFinalizacion,
            nombreEstacion, // 3er argumento: String nombreEstacion
            idSismografo,   // 4to argumento: String idSismografo
            estadoDTO       // 5to argumento: EstadoDTO estado
        );
    }

    // El método al cerrar se corrige de forma similar
    public OrdenInspeccionDTO mapearADTOAlCerrar() {
        
        // 1. Mapear Estado a EstadoDTO
        EstadoDTO estadoDTO = this.estado != null ? this.estado.mapearADTO() : null;

        // 2. Obtener los campos específicos de EstacionSismologica y Sismografo
        String nombreEstacion = null;
        String idSismografo = null;

        if (this.estacionSismologica != null) {
            // El DTO pide nombreEstacion. Usaremos getNombre() en lugar de getCodigoEstacion()
            nombreEstacion = this.estacionSismologica.getNombre();

            if (this.estacionSismologica.getSismografo() != null) {
                idSismografo = this.estacionSismologica.getSismografo().getIdentificadorSismografo();
            }
        }

        // Se llama al constructor de OrdenInspeccionDTO con 5 argumentos:
        return new OrdenInspeccionDTO(
            this.nroOrden,
            this.fechaHoraFinalizacion,
            nombreEstacion,
            idSismografo,
            estadoDTO
        );
    }

    // --- Métodos de Negocio ---
    /**
     * Verifica si esta orden fue asignada al Empleado pasado como parámetro.
     */
    public boolean esDeEmpleado(Empleado empleado) {
        return this.empleado != null && this.empleado.getMail().equals(empleado.getMail());
    }

    /**
     * Verifica si el estado actual de la orden es "Completamente Realizado".
     */
    public boolean sosEstadoCompletamenteRealizado() {
        return this.estado != null && this.estado.esCompletamenteRealizada();
    }

    /**
     * Cierra la Orden de Inspección, actualizando su estado, fecha de cierre y observación.
     */
    public void cerrar(Estado estadoCerrada, LocalDateTime fechaYHoraActual, String observacion) {
        this.estado = estadoCerrada;
        this.fechaHoraCierre = fechaYHoraActual;
        this.observacionCierre = observacion;
    }

    /**
     * Delega a la Estación Sismológica (y por ende al Sismógrafo) la
     * responsabilidad de cambiar el estado a "Fuera de Servicio".
     */
    public void enviarSismografoParaReparacion(Estado estadoFS, LocalDateTime fechaYHoraActual, Empleado empleadoRI,
                                            List<MotivoTipo> todosLosMotivos, Map<String, String> motivosSeleccionadosConComentario, List<String> mailsResponsablesReparacion) {
        if (this.estacionSismologica != null) {
            this.estacionSismologica.setSismografoFueraDeServicio(
                    estadoFS,
                    fechaYHoraActual,
                    empleadoRI,
                    todosLosMotivos,
                    motivosSeleccionadosConComentario,
                    mailsResponsablesReparacion
            );
        } else {
            System.err.println("OrdenInspeccion: Advertencia: No hay estación sismológica asociada para enviar el sismógrafo a reparación.");
        }
    }

    // --- Getters y Setters ---
    public Integer getNroOrden() {
        return nroOrden;
    }

    public LocalDateTime getFechaHoraCierre() {
        return fechaHoraCierre;
    }

    public LocalDateTime getFechaHoraFinalizacion() {
        return fechaHoraFinalizacion;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
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

    public EstacionSismologica getEstacionSismologica() {
        return estacionSismologica;
    }
}