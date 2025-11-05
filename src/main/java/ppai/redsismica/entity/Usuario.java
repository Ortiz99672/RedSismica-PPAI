package ppai.redsismica.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

@Entity
@Table(name = "usuarios")
public class Usuario {

    /**
     * Clave Primaria (PK) natural.
     */
    @Id
    @Column(length = 50)
    private String nombreUsuario;

    private String contrasena;

    /**
     * Esta es la relación 'getRILogueado()'.
     * Le decimos a JPA que la columna 'empleado_mail' en esta tabla
     * se une con la columna 'mail' de la entidad Empleado.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_mail", referencedColumnName = "mail")
    private Empleado empleado;

    /**
     * Mapeo inverso a Sesion. 'mappedBy' indica que Sesion
     * es la dueña de la relación (en su campo 'usuario').
     */
    @OneToOne(mappedBy = "usuario", fetch = FetchType.LAZY)
    private Sesion sesion;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Usuario() {
    }

    // --- Getters y Setters Manuales ---

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Este método implementa la lógica de tu diagrama: getRILogueado()
     */
    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Sesion getSesion() {
        return sesion;
    }

    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
    }
}
