package ppai.redsismica.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import ppai.redsismica.dto.EstadoDTO;

/**
 * Entidad que representa el Estado de una entidad (e.g., OrdenInspeccion, Sismografo).
 * El campo `nombreEstado` es la clave primaria.
 */
@Entity
public class Estado {

    @Id
    private String nombreEstado; // Clave primaria
    private String ambito;

    // --- Constructores ---
    public Estado() {
    }

    public Estado(String nombreEstado, String ambito) {
        this.nombreEstado = nombreEstado;
        this.ambito = ambito;
    }

    // --- Métodos de Negocio ---
    /**
     * Verifica si el ámbito del estado es "OrdenInspeccion".
     */
    public boolean esAmbitoOI() {
        return "OrdenInspeccion".equalsIgnoreCase(this.ambito);
    }

    /**
     * Verifica si el ámbito del estado es "Sismografo".
     */
    public boolean esAmbitoSismografo() {
        return "Sismografo".equalsIgnoreCase(this.ambito);
    }

    /**
     * Verifica si el estado es "Cerrada".
     */
    public boolean esCerrada() {
        return "Cerrada".equalsIgnoreCase(this.nombreEstado);
    }

    /**
     * Verifica si el estado es "Fuera de Servicio".
     */
    public boolean esFueraDeServicio() {
        return "Fuera de Servicio".equalsIgnoreCase(this.nombreEstado);
    }

    /**
     * Verifica si el estado es "Completamente Realizada".
     */
    public boolean esCompletamenteRealizada() {
        return "Completamente Realizada".equalsIgnoreCase(this.nombreEstado);
    }

    // --- Método de Mapeo ---
    public EstadoDTO mapearADTO() {
        return new EstadoDTO(this.nombreEstado, this.ambito);
    }

    // --- Getters y Setters ---
    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }
}