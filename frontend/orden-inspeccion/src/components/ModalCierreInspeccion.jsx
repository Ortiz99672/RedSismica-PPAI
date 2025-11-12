import React from 'react';
import Modal from './Modal';
import './ModalCierreInspeccion.css';

/**
 * Componente Modal específico para mostrar la notificación de cierre de inspección
 * @param {boolean} isOpen - Controla si el modal está abierto
 * @param {function} onClose - Función que se ejecuta al cerrar el modal
 * @param {object} notificacion - Objeto con los datos de la notificación
 * @param {function} onVolverAtras - Función que se ejecuta al hacer clic en "Volver a Cerrar Otra Orden"
 */
function ModalCierreInspeccion({ isOpen, onClose, notificacion, onVolverAtras }) {
    if (!notificacion) return null;

    const formatearFecha = (fechaHora) => {
        if (!fechaHora) return 'N/A';
        const fecha = new Date(fechaHora);
        const dia = String(fecha.getDate()).padStart(2, '0');
        const mes = String(fecha.getMonth() + 1).padStart(2, '0');
        const año = fecha.getFullYear();
        const hora = String(fecha.getHours()).padStart(2, '0');
        const minutos = String(fecha.getMinutes()).padStart(2, '0');
        const segundos = String(fecha.getSeconds()).padStart(2, '0');
        return `${dia}/${mes}/${año}, ${hora}:${minutos}:${segundos}`;
    };

    const handleVolverAtras = () => {
        if (onVolverAtras) {
            onVolverAtras();
        }
        onClose();
    };

    return (
        <Modal isOpen={isOpen} onClose={onClose} title="Cierre de Orden de Inspección">
            <div className="modal-cierre-content">
                <div className="notificacion-mensaje">
                    <p className="mensaje-exito">
                        Notificación recibida: El sismógrafo ha sido actualizado y se han enviado los correos.
                    </p>
                </div>

                <div className="notificacion-titulo">
                    <h3>Notificación Generada con Éxito</h3>
                </div>

                <div className="notificacion-detalles">
                    <div className="detalle-item">
                        <span className="detalle-label">Sismógrafo:</span>
                        <span className="detalle-valor">{notificacion.idSismografo || 'N/A'}</span>
                    </div>

                    <div className="detalle-item">
                        <span className="detalle-label">Nuevo Estado:</span>
                        <span className="detalle-valor">{notificacion.estadoSismografo || 'N/A'}</span>
                    </div>

                    <div className="detalle-item">
                        <span className="detalle-label">Fecha y Hora:</span>
                        <span className="detalle-valor">{formatearFecha(notificacion.fechaHora)}</span>
                    </div>
                </div>

                {notificacion.motivos && Object.keys(notificacion.motivos).length > 0 && (
                    <div className="notificacion-seccion">
                        <h4>Motivos Reportados:</h4>
                        <ul className="motivos-lista">
                            {Object.entries(notificacion.motivos).map(([motivo, comentario]) => (
                                <li key={motivo}>
                                    <strong>{motivo}:</strong> {comentario}
                                </li>
                            ))}
                        </ul>
                    </div>
                )}

                {notificacion.mailsResponsables && notificacion.mailsResponsables.length > 0 && (
                    <div className="notificacion-seccion">
                        <h4>Se notificará a:</h4>
                        <ul className="destinatarios-lista">
                            {notificacion.mailsResponsables.map((mail, index) => (
                                <li key={index}>{mail}</li>
                            ))}
                        </ul>
                    </div>
                )}

                <div className="modal-cierre-actions">
                    <button 
                        className="btn-primary" 
                        onClick={handleVolverAtras}
                    >
                        Volver a Cerrar Otra Orden
                    </button>
                </div>
            </div>
        </Modal>
    );
}

export default ModalCierreInspeccion;

