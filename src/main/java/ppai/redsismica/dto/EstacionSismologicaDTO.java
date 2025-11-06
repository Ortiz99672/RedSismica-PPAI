package ppai.redsismica.dto;

public class EstacionSismologicaDTO {
    private String codigoEstacion;
    private String nombre;
    private SismografoDTO sismografo; // Anidamos el DTO

    public EstacionSismologicaDTO(String codigoEstacion, String nombre, SismografoDTO sismografo) {
        this.codigoEstacion = codigoEstacion;
        this.nombre = nombre;
        this.sismografo = sismografo;
    }

    // Getters y Setters
    public String getCodigoEstacion() { return codigoEstacion; }
    public void setCodigoEstacion(String codigoEstacion) { this.codigoEstacion = codigoEstacion; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public SismografoDTO getSismografo() { return sismografo; }
    public void setSismografo(SismografoDTO sismografo) { this.sismografo = sismografo; }
}
