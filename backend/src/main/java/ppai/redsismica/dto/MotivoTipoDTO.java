package ppai.redsismica.dto;

/**
 * DTO para la entidad MotivoTipo.
 */
public class MotivoTipoDTO {

    private String descripcion;

    // Constructor para el mapeo
    public MotivoTipoDTO(String descripcion) {
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
