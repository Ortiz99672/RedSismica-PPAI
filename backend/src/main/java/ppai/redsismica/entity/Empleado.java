package ppai.redsismica.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import ppai.redsismica.dto.EmpleadoDTO;
import ppai.redsismica.dto.RolDTO;

/**
 * Entidad que representa a un Empleado del sistema.
 * El campo `mail` es la clave primaria.
 */
@Entity
public class Empleado {

    @Id
    private String mail; // Clave primaria
    private String apellido;
    private String nombre;
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "rol_nombre")
    private Rol rol;

    // --- Constructores ---
    public Empleado() {
    }

    public Empleado(String mail, String apellido, String nombre, String telefono, Rol rol) {
        this.mail = mail;
        this.apellido = apellido;
        this.nombre = nombre;
        this.telefono = telefono;
        this.rol = rol;
    }

    // --- Método de Mapeo ---
    /**
     * Mapea la entidad a un DTO, delegando el mapeo de Rol.
     */
    public EmpleadoDTO mapearADTO() {
        RolDTO rolDTO = (this.rol != null) ? this.rol.mapearADTO() : null;

        return new EmpleadoDTO(
                this.mail,
                this.apellido,
                this.nombre,
                this.telefono,
                rolDTO
        );
    }

    // --- Métodos de Negocio ---
    /**
     * Verifica si el Rol asociado al Empleado es "Responsable de Reparacion".
     */
    public boolean esResponsableDeReparacion() {
        return this.rol != null && this.rol.esRolResponsableDeReparacion();
    }

    /**
     * Retorna la dirección de correo electrónico del Empleado.
     */
    public String obtenerMail() {
        return this.mail;
    }

    // --- Getters y Setters ---
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
    public Rol getRol() {
        return rol;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }
}