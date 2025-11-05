package ppai.redsismica.dto;

import java.time.LocalDateTime;

/**
 * DTO para Sesion.
 * Expone las fechas de la sesión y el DTO del usuario logueado.
 */
public class SesionDTO {

    private LocalDateTime fechaHoraDesde;
    private LocalDateTime fechaHoraHasta;
    private UsuarioDTO usuario; // Anidamos el DTO de Usuario

    // Constructor vacío
    public SesionDTO() {
    }

    // Constructor para facilitar la creación
    public SesionDTO(LocalDateTime fechaHoraDesde, LocalDateTime fechaHoraHasta, UsuarioDTO usuario) {
        this.fechaHoraDesde = fechaHoraDesde;
        this.fechaHoraHasta = fechaHoraHasta;
        this.usuario = usuario;
    }

    // --- Getters y Setters ---

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

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }
}
