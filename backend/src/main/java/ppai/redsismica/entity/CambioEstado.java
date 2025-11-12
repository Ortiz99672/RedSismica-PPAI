package ppai.redsismica.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

/**
 * Entidad que representa un registro de cambio de estado de un Sismógrafo o una Orden de Inspección.
 * La clave primaria es la combinación de la entidad asociada y la fechaHoraInicio.
 */
@Entity
public class CambioEstado {

    @Id
    private LocalDateTime fechaHoraInicio;

    private LocalDateTime fechaHoraFin;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "cambio_estado_motivos", // Tabla de unión para la relación
            joinColumns = @JoinColumn(name = "cambio_estado_fecha_hora_inicio"), // FK a esta entidad
            inverseJoinColumns = @JoinColumn(name = "motivos_fuera_servicio_comentario") // FK a la entidad MotivoFueraDeServicio (usando su @Id)
    )
    private List<MotivoFueraDeServicio> motivosFueraServicio = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "estado_nombre")
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "empleado_mail")
    private Empleado empleado; // Empleado que realizó el cambio de estado (e.g., el RI que cierra la OI)

    // --- Constructores ---
    public CambioEstado() {
    }

    /**
     * Constructor llamado por Sismografo para crear un nuevo registro de estado (e.g., Fuera de Servicio).
     * @param fechaHoraInicio Fecha y hora en que inicia el estado.
     * @param estado Nuevo estado.
     * @param empleado Empleado responsable del cambio (e.g., el RI).
     * @param motivos Map<MotivoTipo, Comentario> con los motivos del fuera de servicio.
     * @param todosLosMotivos Lista completa de MotivoTipo (para buscar por descripción).
     */
    public CambioEstado(LocalDateTime fechaHoraInicio, Estado estado, Empleado empleado, Map<String, String> motivos, List<MotivoTipo> todosLosMotivos) {
        this.fechaHoraInicio = fechaHoraInicio;
        this.estado = estado;
        this.empleado = empleado;

        // Itera sobre los motivos seleccionados para crear MotivoFueraDeServicio
        if (motivos != null && !motivos.isEmpty() && todosLosMotivos != null) {
            for (Map.Entry<String, String> entry : motivos.entrySet()) {
                String descripcionMotivo = entry.getKey();
                String comentario = entry.getValue();

                // Busca el MotivoTipo asociado por la descripción
                MotivoTipo motivoTipo = todosLosMotivos.stream()
                        .filter(m -> m.getDescripcion().equals(descripcionMotivo))
                        .findFirst()
                        .orElse(null);

                if (motivoTipo != null) {
                    MotivoFueraDeServicio nuevoMotivo = new MotivoFueraDeServicio(comentario, motivoTipo);
                    this.motivosFueraServicio.add(nuevoMotivo);
                } else {
                    System.err.println("Advertencia: No se encontró MotivoTipo para la descripción: " + descripcionMotivo);
                }
            }
        }
    }

    public CambioEstado(LocalDateTime fechaHoraInicio, Estado estado, Empleado empleado) {
        this.fechaHoraInicio = fechaHoraInicio;
        this.estado = estado;
        this.empleado = empleado;
    }

    public void crearMotivosFueraDeServicio(Map<String, String> motivosConComentarios, List<MotivoTipo> todosLosMotivos) {
        System.out.println("CambioEstado: Ejecutando 7.1.12.1.3.2 crearMotivosFueraDeServicio()...");

        if (this.motivosFueraServicio == null) {
            this.motivosFueraServicio = new ArrayList<>();
        }

        for (Map.Entry<String, String> entry : motivosConComentarios.entrySet()) {
            String descripcionMotivo = entry.getKey();
            String comentario = entry.getValue();

            // Busca el MotivoTipo asociado por la descripción
            MotivoTipo motivoTipo = todosLosMotivos.stream()
                    .filter(m -> m.getDescripcion().equals(descripcionMotivo))
                    .findFirst()
                    .orElse(null);

            if (motivoTipo != null) {
                // Paso 9: Crea el nuevo MotivoFueraDeServicio
                MotivoFueraDeServicio nuevoMotivo = new MotivoFueraDeServicio(comentario, motivoTipo);
                
                // Añadimos el nuevo motivo a la lista de este CambioEstado
                this.motivosFueraServicio.add(nuevoMotivo);
            } else {
                System.err.println("Advertencia: No se encontró MotivoTipo para la descripción: " + descripcionMotivo);
            }
        }
    }

    public boolean esEstadoActual() {
        // Asumimos que el estado actual es el que tiene fechaHoraFin en NULL
        return this.fechaHoraFin == null;
    }

    // --- Métodos de Negocio ---

    /**
     * Establece la fecha y hora de finalización de este registro de cambio de estado.
     */
    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    // --- Getters y Setters ---
    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public List<MotivoFueraDeServicio> getMotivosFueraServicio() {
        return motivosFueraServicio;
    }

    public Estado getEstado() {
        return estado;
    }

    public Empleado getEmpleado() {
        return empleado;
    }
}