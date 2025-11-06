package ppai.redsismica.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import ppai.redsismica.dto.SismografoDTO;

@Entity
public class Sismografo {

    @Id
    private String nroSerie;

    private LocalDate fechaAdquisicion;
    private String identificadorSismografo;

    @OneToOne
    @JoinColumn(name = "estacion_sismologica_codigo")
    private EstacionSismologica estacionSismologica;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sismografo_nro_serie")
    private List<CambioEstado> cambioDeEstado;

    // --- Constructores ---
    public Sismografo() {
    }

    public Sismografo(String nroSerie, LocalDate fechaAdquisicion, String identificadorSismografo, EstacionSismologica estacionSismologica) {
        this.nroSerie = nroSerie;
        this.fechaAdquisicion = fechaAdquisicion;
        this.identificadorSismografo = identificadorSismografo;
        this.estacionSismologica = estacionSismologica;
    }

    // --- Métodos del diagrama ---
    public String getIdentificadorSismografo() {
        return this.identificadorSismografo;
    }

    /**
     * Implementa 1.2.6.3: sosDeEstacionSismologica()
     * (El diagrama está un poco confuso aquí, pero asumimos
     * que solo verifica que pertenece a una estación)
     */
    public boolean sosDeEstacionSismologica() {
        return this.estacionSismologica != null;
    }

    public void obtenerEstadoActual() {
        // Lógica a implementar
    }

    public void crearCambioEstado() {
        // Lógica a implementar
    }

    public void enviarAReparar() {
        // Lógica a implementar
    }

    public void ponerFueraDeServicio() {
        // Lógica a implementar
    }

    public boolean esEstadoActual() {
        // Lógica futura
        System.out.println("Sismografo: STUB - esEstadoActual()");
        return true;
    }

    // --- Método de Mapeo ---
    public SismografoDTO mapearADTO() {
        return new SismografoDTO(this.nroSerie, this.identificadorSismografo);
    }

    // --- Getters y Setters adicionales ---
    public String getNroSerie() {
        return nroSerie;
    }

    public void setNroSerie(String nroSerie) {
        this.nroSerie = nroSerie;
    }

    public LocalDate getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(LocalDate fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public void setIdentificadorSismografo(String identificadorSismografo) {
        this.identificadorSismografo = identificadorSismografo;
    }

    public EstacionSismologica getEstacionSismologica() {
        return estacionSismologica;
    }

    public void setEstacionSismologica(EstacionSismologica estacionSismologica) {
        this.estacionSismologica = estacionSismologica;
    }

    public List<CambioEstado> getCambioDeEstado() {
        return cambioDeEstado;
    }

    public void setCambioDeEstado(List<CambioEstado> cambioDeEstado) {
        this.cambioDeEstado = cambioDeEstado;
    }
}
