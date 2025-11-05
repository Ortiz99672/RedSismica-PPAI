package ppai.redsismica.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;


/**
 * Clase que representa MotivoTipo según el diagrama de clases.
 * - La clave primaria es 'descripcion'
 * - Métodos definidos: descripcion() y getDescripcion()
 */
@Entity
@Table(name = "motivo_tipo")
public class MotivoTipo {
    @Id
    @Column(length = 100, nullable = false, unique = true)
    private String descripcion;

    public MotivoTipo() {
    }

    public MotivoTipo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Setter implícito (no mostrado en el diagrama pero necesario en Java).
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
