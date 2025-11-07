package ppai.redsismica.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "motivo_fuera_de_servicio")
public class MotivoFueraDeServicio {
    @Id
    @Column(length = 255, nullable = false, unique = true)
    private String comentario;

    // Relación uno a uno con MotivoTipo
    @OneToOne(optional = false)
    @JoinColumn(name = "motivo_tipo", referencedColumnName = "descripcion")
    private MotivoTipo motivoTipo;

    public MotivoFueraDeServicio() {
    }

    /**
     * Constructor "new()" (Paso 9) según el diagrama.
     * Crea una instancia de MotivoFueraServicio con comentario y motivoTipo.
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
