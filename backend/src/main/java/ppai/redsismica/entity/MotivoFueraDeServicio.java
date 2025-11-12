package ppai.redsismica.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Entidad que representa un Motivo por el cual un Sismógrafo pasa a Fuera de Servicio.
 * NOTA: El campo `comentario` se usa como clave primaria (`@Id`) debido a las restricciones
 * de la tabla de unión en `CambioEstado`. Esto asume que el comentario es único
 * para todos los MotivosFueraDeServicio, lo cual es una decisión de diseño arriesgada.
 */
@Entity
@Table(name = "motivo_fuera_de_servicio")
public class MotivoFueraDeServicio {
    @Id
    @Column(length = 255, nullable = false, unique = true)
    private String comentario; // Clave primaria: ¡Debe ser única!

    // Relación uno a uno con MotivoTipo (opcional = false porque siempre debe estar)
    @OneToOne(optional = false)
    @JoinColumn(name = "motivo_tipo", referencedColumnName = "descripcion")
    private MotivoTipo motivoTipo;

    public MotivoFueraDeServicio() {
    }

    /**
     * Constructor para crear una instancia de MotivoFueraServicio con comentario y motivoTipo.
     * @param comentario Comentario asociado.
     * @param motivoTipo El tipo de motivo (e.g., "Falla de sensor").
     */
    public MotivoFueraDeServicio(String comentario, MotivoTipo motivoTipo) {
        this.comentario = comentario;
        this.motivoTipo = motivoTipo;
    }

    // ======= Getters y Setters =======

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public MotivoTipo getMotivoTipo() {
        return motivoTipo;
    }

    public void setMotivoTipo(MotivoTipo motivoTipo) {
        this.motivoTipo = motivoTipo;
    }
}