package ppai.redsismica.dto;

/**
 * DTO para Usuario.
 * Expone el nombre de usuario y los datos del empleado (RILogueado)
 * NUNCA expone la contraseña.
 */
public class UsuarioDTO {

    private String nombreUsuario;
    private EmpleadoDTO empleado; // Esto representa el 'RILogueado'

    // Constructor vacío
    public UsuarioDTO() {
    }

    // Constructor para facilitar la creación
    public UsuarioDTO(String nombreUsuario, EmpleadoDTO empleado) {
        this.nombreUsuario = nombreUsuario;
        this.empleado = empleado;
    }

    // --- Getters y Setters ---

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public EmpleadoDTO getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoDTO empleado) {
        this.empleado = empleado;
    }
}
