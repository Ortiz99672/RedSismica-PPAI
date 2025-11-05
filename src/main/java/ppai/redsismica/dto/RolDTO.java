package ppai.redsismica.dto;

/**
 * DTO para Rol.
 * Contiene los datos públicos de un rol.
 */
public class RolDTO {

    private String nombre;
    private String descripcion;

    /**
     * Constructor vacío.
     */
    public RolDTO() {
    }

    /**
     * Constructor completo para facilitar el mapeo.
     */
    public RolDTO(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // --- Getters y Setters Manuales ---

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
