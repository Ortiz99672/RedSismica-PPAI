package ppai.redsismica.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import ppai.redsismica.dto.SismografoDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;


@Entity
public class Sismografo {

    @Id
    private String nroSerie;

    private LocalDate fechaAdquisicion;
    private String identificadorSismografo;

    @OneToOne
    @JoinColumn(name = "estacion_sismologica_codigo")
    private EstacionSismologica estacionSismologica;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "sismografo_nro_serie")
    private List<CambioEstado> cambioDeEstado = new ArrayList<>();

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

    /**
     * 7.1.12.1.1.1: Implementación de "obtenerEstadoActual"
     * Busca en la lista de cambios de estado cuál es el actual
     * (el que no tiene fecha de fin).
     */
    public CambioEstado obtenerEstadoActual() {
        System.out.println("Sismografo: Ejecutando 7.1.12.1.1.1 obtenerEstadoActual()...");
        if (this.cambioDeEstado == null) {
            return null;
        }
        for (CambioEstado ce : this.cambioDeEstado) {
            if (ce.esEstadoActual()) {
                return ce;
            }
        }
        return null; // O manejar error si no se encuentra ninguno
    }

    /**
     * 7.1.12.1.3: Implementación de "crearCambioEstado"
     * Crea la nueva instancia de CambioEstado y le delega
     * la creación de los motivos.
     */
    public void crearCambioEstado(
            Estado estadoNuevo,
            LocalDateTime fechaHora,
            Empleado empleado,
            List<MotivoTipo> todosLosMotivos,
            Map<String, String> motivosConComentarios
    ) {
        System.out.println("Sismografo: Ejecutando 7.1.12.1.3 crearCambioEstado()...");

        // 7.1.12.1.3.1: new CambioEstado()
        // Le pasamos el empleado (RI logueado)
        CambioEstado nuevoCambio = new CambioEstado(fechaHora, estadoNuevo, empleado);

        // 8: Llama a crearMotivosFueraDeServicio
        nuevoCambio.crearMotivosFueraDeServicio(motivosConComentarios, todosLosMotivos);

        // Añadimos el nuevo estado (con sus motivos) a la lista del sismógrafo
        this.cambioDeEstado.add(nuevoCambio);

        System.out.println("Sismografo: Nuevo CambioEstado creado y añadido.");
    }

    /**
     * 7.1.12.1.1: Implementación de "enviarAReparar"
     */
    public void enviarAReparar(
            Estado estadoSismografo,
            LocalDateTime fechaHora,
            Empleado empleado,
            List<MotivoTipo> todosLosMotivos,
            Map<String, String> motivosConComentarios
    ) {
        System.out.println("Sismografo: Ejecutando 7.1.12.1.1 enviarAReparar()...");

        // 7.1.12.1.1.1: Llama a obtenerEstadoActual
        CambioEstado estadoActual = this.obtenerEstadoActual();

        if (estadoActual != null) {
            // 7.1.12.1.1.2: Llama a setFechaHoraFin
            estadoActual.setFechaHoraFin(fechaHora);
        } else {
            System.out.println("Sismografo: No se encontró estado actual para finalizar.");
        }

        // 7.1.12.1.3: Llama a crearCambioEstado
        this.crearCambioEstado(estadoSismografo, fechaHora, empleado, todosLosMotivos, motivosConComentarios);
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
