package ppai.redsismica.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

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
