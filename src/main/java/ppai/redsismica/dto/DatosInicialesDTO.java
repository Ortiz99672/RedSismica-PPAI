package ppai.redsismica.dto;

import java.util.List;

/**
 * Agrupa todos los datos que la pantalla necesita
 * al cargar por primera vez.
 */
public class DatosInicialesDTO {

    private EmpleadoDTO riLogueado;
    private List<OrdenInspeccionDTO> ordenes;

    public DatosInicialesDTO(EmpleadoDTO riLogueado, List<OrdenInspeccionDTO> ordenes) {
        this.riLogueado = riLogueado;
        this.ordenes = ordenes;
    }

    // Getters y Setters
    public EmpleadoDTO getRiLogueado() { return riLogueado; }
    public void setRiLogueado(EmpleadoDTO riLogueado) { this.riLogueado = riLogueado; }
    public List<OrdenInspeccionDTO> getOrdenes() { return ordenes; }
    public void setOrdenes(List<OrdenInspeccionDTO> ordenes) { this.ordenes = ordenes; }
}
