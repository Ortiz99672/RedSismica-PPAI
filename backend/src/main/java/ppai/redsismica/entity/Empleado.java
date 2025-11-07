package ppai.redsismica.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import ppai.redsismica.dto.EmpleadoDTO;
import ppai.redsismica.dto.RolDTO;

@Entity
public class Empleado {

    @Id
    private String mail;

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

    // --- NUEVO MÉTODO DE MAPEO ---
    /**
     * Se mapea a sí mismo a un DTO, delegando el mapeo de Rol.
     */
    public EmpleadoDTO mapearADTO() {
        RolDTO rolDTO = null;
        if (this.rol != null) {
            rolDTO = this.rol.mapearADTO(); // Delega la responsabilidad a Rol
        }

        return new EmpleadoDTO(
                this.mail,
                this.apellido,
                this.nombre,
                this.telefono,
                rolDTO
        );
    }

    // --- Métodos del diagrama ---
    public boolean esResponsableDeReparacion() {
        System.out.println("Empleado: Ejecutando 7.1.14 esResponsableDeReparacion()...");
        if (this.rol == null) {
            return false;
        }
        // 7.1.14.1: Delega la lógica al Rol
        // (Usamos un método más expresivo que getNombre())
        return this.rol.esRolResponsableDeReparacion();
    }

    /**
     * 7.1.15: Implementación de "obtenerMail"
     */
    public String obtenerMail() {
        System.out.println("Empleado: Ejecutando 7.1.15 obtenerMail()...");
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
