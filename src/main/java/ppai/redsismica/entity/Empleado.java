package ppai.redsismica.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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

    // --- Métodos del diagrama ---
    public boolean esResponsableDeReparacion() {
        // Lógica a implementar
        return false;
    }

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
