package ppai.redsismica.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

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

    // --- Métodos del diagrama ---
    public boolean esAmbitoOI() {
        // Lógica a implementar
        return "OrdenInspeccion".equalsIgnoreCase(this.ambito);
    }

    public boolean esCerrada() {
        // Lógica a implementar
        return "Cerrada".equalsIgnoreCase(this.nombreEstado);
    }

    public boolean esFueraDeServicio() {
        // Lógica a implementar
        return "Fuera de Servicio".equalsIgnoreCase(this.nombreEstado);
    }

    public boolean esCompletamenteRealizada() {
        // Lógica a implementar
        return "Completamente Realizada".equalsIgnoreCase(this.nombreEstado);
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
