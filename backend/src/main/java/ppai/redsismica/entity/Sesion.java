package ppai.redsismica.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sesion")
public class Sesion {

    /**
     * La Clave Primaria (PK) de Sesion es la relación con Usuario.
     * @MapsId le dice a JPA que la PK de esta entidad
     * es mapeada desde la entidad 'Usuario'.
     */
    @Id
    @Column(name = "usuario_nombreUsuario") // Esta es la columna PK/FK
    private String usuarioNombreUsuario;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Mapea la PK de la relación 'usuario' a nuestro campo @Id 'usuarioNombreUsuario'
    @JoinColumn(name = "usuario_nombreUsuario") // Define la columna de clave foránea
    private Usuario usuario;

    private LocalDateTime fechaHoraDesde;

    private LocalDateTime fechaHoraHasta;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Sesion() {
    }

    // --- Getters y Setters Manuales ---

    // Getter/Setter para el nuevo campo Id
    public String getUsuarioNombreUsuario() {
        return usuarioNombreUsuario;
    }

    public void setUsuarioNombreUsuario(String usuarioNombreUsuario) {
        this.usuarioNombreUsuario = usuarioNombreUsuario;
    }

    /**
     * Este método implementa la lógica de tu diagrama: obtenerUsuario()
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
}
