package ppai.redsismica.dto;

public class SismografoDTO {
    private String nroSerie;
    private String identificadorSismografo;

    public SismografoDTO(String nroSerie, String identificadorSismografo) {
        this.nroSerie = nroSerie;
        this.identificadorSismografo = identificadorSismografo;
    }

    // Getters y Setters
    public String getNroSerie() { return nroSerie; }
    public void setNroSerie(String nroSerie) { this.nroSerie = nroSerie; }
    public String getIdentificadorSismografo() { return identificadorSismografo; }
    public void setIdentificadorSismografo(String identificadorSismografo) { this.identificadorSismografo = identificadorSismografo; }
}
