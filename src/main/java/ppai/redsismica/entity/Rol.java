package ppai.redsismica.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import ppai.redsismica.dto.RolDTO;

@Entity
public class Rol {

    @Id
    private String nombre;
    private String descripcion;

    // --- Constructores ---
    public Rol() {
    }

    public Rol(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // --- NUEVO MÉTODO DE MAPEO ---
    /**
     * Se mapea a sí mismo a un DTO.
     */
    public RolDTO mapearADTO() {
        return new RolDTO(this.nombre, this.descripcion);
    }

    public boolean esRolResponsableDeReparacion() {
        // Asumimos que el nombre del rol es "Responsable de Reparacion"
        // (Esto podría venir de una constante)
        return "Responsable de Reparacion".equalsIgnoreCase(this.nombre);
    }

    // --- Getters y Setters ---
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
