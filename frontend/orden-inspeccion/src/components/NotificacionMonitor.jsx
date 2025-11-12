import React, { useState } from 'react';
import { useObserver } from '../hooks/useObserver';

/**
 * Componente de ejemplo que demuestra cómo otros componentes pueden
 * suscribirse al Observer Service para recibir notificaciones.
 * 
 * Este componente actúa como un "monitor" que muestra las notificaciones
 * en tiempo real cuando se producen cambios en el sistema.
 */
function NotificacionMonitor() {
    const [ultimaNotificacion, setUltimaNotificacion] = useState(null);
    const [contador, setContador] = useState(0);

    // Suscribirse al Observer Service usando el hook personalizado
    useObserver((datosNotificacion) => {
        console.log('NotificacionMonitor: Recibida notificación:', datosNotificacion);
        if (datosNotificacion) {
            setUltimaNotificacion(datosNotificacion);
            setContador(prev => prev + 1);
        }
    }, []);

    if (!ultimaNotificacion) {
        return (
            <div style={{ 
                padding: '1rem', 
                margin: '1rem', 
                border: '1px solid #ccc', 
                borderRadius: '4px',
                backgroundColor: '#f8f9fa'
            }}>
                <h3>Monitor de Notificaciones</h3>
                <p>Esperando notificaciones...</p>
            </div>
        );
    }

    return (
        <div style={{ 
            padding: '1rem', 
            margin: '1rem', 
            border: '2px solid #007bff', 
            borderRadius: '8px',
            backgroundColor: '#e7f3ff'
        }}>
            <h3>Monitor de Notificaciones (Actualizaciones recibidas: {contador})</h3>
            <div style={{ marginTop: '1rem' }}>
                <p><strong>Sismógrafo:</strong> {ultimaNotificacion.idSismografo}</p>
                <p><strong>Estado:</strong> {ultimaNotificacion.estadoSismografo}</p>
                <p><strong>Fecha/Hora:</strong> {new Date(ultimaNotificacion.fechaHora).toLocaleString()}</p>
                <p><strong>Motivos:</strong> {Object.keys(ultimaNotificacion.motivos || {}).length} motivo(s)</p>
                <p><strong>Destinatarios:</strong> {ultimaNotificacion.mailsResponsables?.length || 0} correo(s)</p>
            </div>
        </div>
    );
}

export default NotificacionMonitor;

