package ppai.redsismica.dto;

/**
 * DTO para Rol.
 * (Basado en la definición previa)
 */
public class RolDTO {

    private String nombre;
    private String descripcion;

    public RolDTO() {
    }

    public RolDTO(String nombre, String descripcion) {
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
