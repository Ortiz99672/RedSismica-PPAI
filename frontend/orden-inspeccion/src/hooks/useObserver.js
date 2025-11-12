import { useEffect, useCallback, useRef } from 'react';
import observerService from '../services/ObserverService';

/**
 * Hook personalizado para suscribirse al Observer Service.
 * Facilita el uso del patrón Observer en componentes React.
 * 
 * @param {Function} callback - Función que se ejecutará cuando haya una notificación.
 * @param {Array} deps - Dependencias para el callback (similar a useEffect).
 */
export const useObserver = (callback, deps = []) => {
    const callbackRef = useRef(callback);

    // Actualizar la referencia del callback cuando cambie
    useEffect(() => {
        callbackRef.current = callback;
    }, [callback]);

    useEffect(() => {
        // Crear un wrapper que use la referencia actual del callback
        const wrappedCallback = (datos) => {
            callbackRef.current(datos);
        };

        console.log('useObserver: Suscribiéndose al Observer Service...');
        const desuscribir = observerService.suscribir(wrappedCallback);

        // Limpiar la suscripción cuando el componente se desmonte o cambien las dependencias
        return () => {
            console.log('useObserver: Desuscribiéndose del Observer Service...');
            desuscribir();
        };
    }, deps);
};

export default useObserver;

