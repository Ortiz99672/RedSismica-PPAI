package ppai.redsismica.controller;

/**
 * Interfaz del Patrón Observer.
 * Define el contrato para que la clase Sujeto (GestorCierre)
 * gestione a sus observadores y notifique el evento.
 */
public interface ISujeto {
    /**
     * Agrega un nuevo observador a la lista de suscritos.
     * @param observador La instancia del observador a suscribir.
     */
    void suscribir(IObservador observador);
    /**
     * Remueve un observador de la lista de suscritos.
     * @param observador La instancia del observador a quitar.
     */
    void quitar(IObservador observador);
    /**
     * Itera sobre la lista de observadores y les notifica el cambio de estado.
     */
    void notificar();
}