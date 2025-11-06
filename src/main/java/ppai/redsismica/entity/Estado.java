package ppai.redsismica.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import ppai.redsismica.dto.EstadoDTO;

@Entity
public class Estado {

    @Id
    private String nombreEstado;
    private String ambito;

    // --- Constructores ---
    public Estado() {
    }

    public Estado(String nombreEstado, String ambito) {
        this.nombreEstado = nombreEstado;
        this.ambito = ambito;
    }

    // --- Métodos del diagrama (Implementados) ---
    public boolean esAmbitoOI() {
        return "OrdenInspeccion".equalsIgnoreCase(this.ambito);
    }

    public boolean esAmbitoSismografo() {
        // Asumimos que el ámbito se llama "Sismografo"
        return "Sismografo".equalsIgnoreCase(this.ambito);
    }

    public boolean esCerrada() {
        return "Cerrada".equalsIgnoreCase(this.nombreEstado);
    }

    public boolean esFueraDeServicio() {
        return "Fuera de Servicio".equalsIgnoreCase(this.nombreEstado);
    }

    /**
     * Implementa la lógica de 1.2.5.1: esCRI()
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
    // ... (resto de getters/setters)
    public void setNombreEstado(String nombreEstado) { this.nombreEstado = nombreEstado; }
    public String getAmbito() { return ambito; }
    public void setAmbito(String ambito) { this.ambito = ambito; }
}
