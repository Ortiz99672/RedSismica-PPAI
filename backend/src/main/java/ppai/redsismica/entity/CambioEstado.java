package ppai.redsismica.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Entity
public class CambioEstado {

    @Id
    private LocalDateTime fechaHoraInicio;

    private LocalDateTime fechaHoraFin;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "cambio_estado_motivos", // Nombre de tu tabla de unión
            joinColumns = @JoinColumn(name = "cambio_estado_fecha_hora_inicio"), // FK a esta entidad (CambioEstado)
            inverseJoinColumns = @JoinColumn(name = "motivos_fuera_servicio_comentario") // FK a la entidad en la lista (MotivoFueraDeServicio)
    )
    private List<MotivoFueraDeServicio> motivosFueraServicio = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "estado_nombre")
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "empleado_mail")
    private Empleado empleado;

    // --- Constructores ---
    public CambioEstado() {
    }

    /**
     * 7.1.12.1.3.1: new CambioEstado()
     * Constructor llamado por Sismografo
     */
    public CambioEstado(LocalDateTime fechaHoraInicio, Estado estado, Empleado empleado) {
        System.out.println("CambioEstado: Ejecutando 7.1.12.1.3.1 new() (Constructor)...");
        this.fechaHoraInicio = fechaHoraInicio;
        this.estado = estado;
        this.empleado = empleado;
    }

    // --- Métodos del diagrama ---
    public boolean esEstadoActual() {
        return this.fechaHoraFin == null;
    }

    /**
     * 8: Implementación de "crearMotivosFueraDeServicio"
     * Es llamado por el Sismografo después de crear este CambioEstado.
     */
    public void crearMotivosFueraDeServicio(
            Map<String, String> motivosConComentarios,
            List<MotivoTipo> todosLosMotivos) {

        System.out.println("CambioEstado: Ejecutando 8 crearMotivosFueraDeServicio()...");

        // Loop "mientras haya motivos"
        for (Map.Entry<String, String> entry : motivosConComentarios.entrySet()) {
            String motivoDescripcion = entry.getKey();
            String comentario = entry.getValue();

            // Buscamos la entidad MotivoTipo correspondiente
            MotivoTipo motivoTipo = todosLosMotivos.stream()
                    .filter(m -> m.getDescripcion().equals(motivoDescripcion))
                    .findFirst()
                    .orElse(null);

            if (motivoTipo != null) {
                // 9: new MotivoFueraServicio()
                System.out.println("CambioEstado: Ejecutando 9 new MotivoFueraServicio() para: " + motivoTipo.getDescripcion());
                MotivoFueraDeServicio nuevoMotivo = new MotivoFueraDeServicio(comentario, motivoTipo);

                // Añadimos el nuevo motivo a la lista de este CambioEstado
                this.motivosFueraServicio.add(nuevoMotivo);
            }
        }
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

    /**
     * 7.1.12.1.1.2: Implementación de "setFechaHoraFin"
     */
    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        System.out.println("CambioEstado: Ejecutando 7.1.12.1.1.2 setFechaHoraFin()...");
        this.fechaHoraFin = fechaHoraFin;
    }

    public List<MotivoFueraDeServicio> getMotivosFueraServicio() {
        return motivosFueraServicio;
    }

    public void setMotivosFueraServicio(List<MotivoFueraDeServicio> motivos) {
        this.motivosFueraServicio = motivos;
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
