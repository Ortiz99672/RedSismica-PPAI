package ppai.redsismica.dto;

/**
 * DTO simple para recibir la confirmación de cierre desde el frontend.
 */
public class ConfirmacionDTO {
    private Boolean confirmacion;

    public ConfirmacionDTO() {
    }

    public ConfirmacionDTO(Boolean confirmacion) {
        this.confirmacion = confirmacion;
    }

    public Boolean getConfirmacion() {
        return confirmacion;
    }

    public void setConfirmacion(Boolean confirmacion) {
        this.confirmacion = confirmacion;
    }
}

