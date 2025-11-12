package ppai.redsismica.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import ppai.redsismica.dto.RolDTO;

/**
 * Entidad que representa un Rol en el sistema.
 * El campo `nombre` es la clave primaria.
 */
@Entity
public class Rol {

    @Id
    private String nombre; // Clave primaria
    private String descripcion;

    // --- Constructores ---
    public Rol() {
    }

    public Rol(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // --- Método de Mapeo ---
    /**
     * Mapea la entidad a un DTO.
     */
    public RolDTO mapearADTO() {
        return new RolDTO(this.nombre, this.descripcion);
    }

    /**
     * Verifica si este Rol es el "Responsable de Reparacion".
     */
    public boolean esRolResponsableDeReparacion() {
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