package ppai.redsismica.dto;

/**
 * DTO para Empleado (RILogueado).
 * ACTUALIZADO para anidar el RolDTO completo.
 */
public class EmpleadoDTO {

    private String mail;
    private String apellido;
    private String nombre;
    private String telefono;

    /**
     * Anidamos el DTO de Rol.
     */
    private RolDTO rol;

    /**
     * Constructor vacío.
     */
    public EmpleadoDTO() {
    }

    /**
     * Constructor completo actualizado.
     */
    public EmpleadoDTO(String mail, String apellido, String nombre, String telefono, RolDTO rol) {
        this.mail = mail;
        this.apellido = apellido;
        this.nombre = nombre;
        this.telefono = telefono;
        this.rol = rol;
    }

    // --- Getters y Setters Manuales ---

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Getter y Setter para el RolDTO anidado
    public RolDTO getRol() {
        return rol;
    }

    public void setRol(RolDTO rol) {
        this.rol = rol;
    }
}
