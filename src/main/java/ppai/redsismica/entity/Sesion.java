package ppai.redsismica.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.FetchType;
import java.time.LocalDateTime;

@Entity
@Table(name = "sesiones")
public class Sesion {

    /**
     * La Clave Primaria (PK) de Sesion es la relación con Usuario.
     * @MapsId le dice a JPA que la PK de esta entidad
     * es mapeada desde la entidad 'Usuario'.
     */
    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Indica que la PK de Sesion viene de la relación Usuario
    @JoinColumn(name = "usuario_nombreUsuario") // Columna PK/FK
    private Usuario usuario;

    private LocalDateTime fechaHoraDesde;

    private LocalDateTime fechaHoraHasta;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Sesion() {
    }

    // --- Getters y Setters Manuales ---

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
