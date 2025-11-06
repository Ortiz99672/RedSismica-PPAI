package ppai.redsismica.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.util.List;
import ppai.redsismica.dto.EstacionSismologicaDTO;
import ppai.redsismica.dto.SismografoDTO;

@Entity
public class EstacionSismologica {

    @Id
    private String codigoEstacion;

    private String documentoCertificacionAdd;
    private LocalDate fechaSolicitudCertificacion;
    private Double latitud;
    private Double longitud;
    private String nombre;
    private String nroCertificacionAdquisicion;

    @OneToOne (mappedBy = "estacionSismologica")
    private Sismografo sismografo;

    // --- Constructores ---
    public EstacionSismologica() {
    }

    public EstacionSismologica(String codigoEstacion, String documentoCertificacionAdd, LocalDate fechaSolicitudCertificacion, Double latitud, Double longitud, String nombre, String nroCertificacionAdquisicion) {
        this.codigoEstacion = codigoEstacion;
        this.documentoCertificacionAdd = documentoCertificacionAdd;
        this.fechaSolicitudCertificacion = fechaSolicitudCertificacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.nroCertificacionAdquisicion = nroCertificacionAdquisicion;
    }

    // --- Métodos del diagrama ---
    public String getNombre() {
        return this.nombre;
    }

    public String obtenerIdSismografo() {
        if (this.sismografo != null) {
            return this.sismografo.getNroSerie();
        }
        return null;
    }

    public void ponerSismografoFueraDeServicio() {
        if (this.sismografo != null) {
            this.sismografo.ponerFueraDeServicio();
        } else {
            System.out.println("EstacionSismologica: No hay sismógrafo para poner fuera de servicio.");
        }
    }

    // --- Método de Mapeo ---
    public EstacionSismologicaDTO mapearADTO() {
        SismografoDTO sismografoDTO = null;
        if (this.sismografo != null) {
            // Delegamos el mapeo al sismógrafo
            sismografoDTO = this.sismografo.mapearADTO();
        }
        return new EstacionSismologicaDTO(this.codigoEstacion, this.nombre, sismografoDTO);
    }

    // --- Getters y Setters adicionales ---
    public String getCodigoEstacion() {
        return codigoEstacion;
    }

    public void setCodigoEstacion(String codigoEstacion) {
        this.codigoEstacion = codigoEstacion;
    }

    public String getDocumentoCertificacionAdd() {
        return documentoCertificacionAdd;
    }

    public void setDocumentoCertificacionAdd(String documentoCertificacionAdd) {
        this.documentoCertificacionAdd = documentoCertificacionAdd;
    }

    public LocalDate getFechaSolicitudCertificacion() {
        return fechaSolicitudCertificacion;
    }

    public void setFechaSolicitudCertificacion(LocalDate fechaSolicitudCertificacion) {
        this.fechaSolicitudCertificacion = fechaSolicitudCertificacion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNroCertificacionAdquisicion() {
        return nroCertificacionAdquisicion;
    }

    public void setNroCertificacionAdquisicion(String nroCertificacionAdquisicion) {
        this.nroCertificacionAdquisicion = nroCertificacionAdquisicion;
    }

    public Sismografo getSismografo() {
        return sismografo;
    }

    public void setSismografo(Sismografo sismografo) {
        this.sismografo = sismografo;
    }
}
