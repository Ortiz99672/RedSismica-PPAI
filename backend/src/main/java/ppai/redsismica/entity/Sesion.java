package ppai.redsismica.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Entidad que registra una Sesión de Usuario.
 * Utiliza una clave primaria compartida (`@MapsId`) con la entidad Usuario.
 */
@Entity
@Table(name = "sesion")
public class Sesion {

    @Id
    @Column(name = "usuario_nombreUsuario") // Columna PK/FK
    private String usuarioNombreUsuario;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Mapea la PK de Usuario a nuestro campo @Id 'usuarioNombreUsuario'
    @JoinColumn(name = "usuario_nombreUsuario") // Define la columna de clave foránea
    private Usuario usuario;

    private LocalDateTime fechaHoraDesde;
    private LocalDateTime fechaHoraHasta;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Sesion() {
    }

    // --- Getters y Setters ---

    /**
     * Retorna el Usuario asociado a esta Sesión.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaHoraDesde() {
        return fechaHoraDesde;
    }

    public void setFechaHoraDesde(LocalDateTime fechaHoraDesde) {
        this.fechaHoraDesde = fechaHoraDesde;
    }

    public LocalDateTime getFechaHoraHasta() {
        return fechaHoraHasta;
    }

    public void setFechaHoraHasta(LocalDateTime fechaHoraHasta) {
        this.fechaHoraHasta = fechaHoraHasta;
    }

    public String getUsuarioNombreUsuario() {
        return usuarioNombreUsuario;
    }

    public void setUsuarioNombreUsuario(String usuarioNombreUsuario) {
        this.usuarioNombreUsuario = usuarioNombreUsuario;
    }
}