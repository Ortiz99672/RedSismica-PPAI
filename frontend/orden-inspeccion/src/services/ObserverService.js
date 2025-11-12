/**
 * Servicio que implementa el Patrón Observer en el frontend.
 * Permite que los componentes React se suscriban y reciban notificaciones
 * cuando ocurren cambios en el estado del sistema.
 */
class ObserverService {
    constructor() {
        this.observadores = [];
    }

    /**
     * Suscribe un observador al servicio.
     * @param {Function} callback - Función que se ejecutará cuando haya una notificación.
     * @returns {Function} - Función para desuscribirse.
     */
    suscribir(callback) {
        if (typeof callback !== 'function') {
            console.error('ObserverService: El callback debe ser una función');
            return () => {};
        }

        this.observadores.push(callback);
        console.log(`ObserverService: Observador suscrito. Total: ${this.observadores.length}`);

        // Retorna una función para desuscribirse
        return () => {
            this.quitar(callback);
        };
    }

    /**
     * Remueve un observador de la lista de suscritos.
     * @param {Function} callback - Función del observador a remover.
     */
    quitar(callback) {
        const index = this.observadores.indexOf(callback);
        if (index > -1) {
            this.observadores.splice(index, 1);
            console.log(`ObserverService: Observador removido. Total: ${this.observadores.length}`);
        }
    }

    /**
     * Notifica a todos los observadores suscritos.
     * @param {Object} datos - Datos de la notificación (NotificacionDTO).
     */
    notificar(datos) {
        console.log(`ObserverService: Notificando a ${this.observadores.length} observadores...`);
        
        this.observadores.forEach((callback, index) => {
            try {
                callback(datos);
            } catch (error) {
                console.error(`ObserverService: Error al notificar al observador ${index}:`, error);
            }
        });
    }

    /**
     * Limpia todos los observadores suscritos.
     */
    desuscribirTodos() {
        console.log('ObserverService: Desuscribiendo todos los observadores...');
        this.observadores = [];
    }
}

// Exportar una instancia singleton del servicio
const observerService = new ObserverService();
export default observerService;

