package ppai.redsismica.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import ppai.redsismica.dto.MotivoTipoDTO;


/**
 * Entidad que representa un Tipo de Motivo.
 * La clave primaria es `descripcion`.
 */
@Entity
@Table(name = "motivo_tipo")
public class MotivoTipo {
    @Id
    @Column(length = 100, nullable = false, unique = true)
    private String descripcion; // Clave primaria

    public MotivoTipo() {
    }

    public MotivoTipo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // --- Método de Mapeo ---
    public MotivoTipoDTO mapearADTO() {
        return new MotivoTipoDTO(this.descripcion);
    }

    /**
     * Setter requerido por JPA.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}