import React from 'react';
import Modal from './Modal';
import './ModalCancelacion.css';

/**
 * Componente Modal para mostrar la cancelación del cierre
 * @param {boolean} isOpen - Controla si el modal está abierto
 * @param {function} onClose - Función que se ejecuta al cerrar el modal
 * @param {function} onAceptar - Función que se ejecuta al aceptar la cancelación
 */
function ModalCancelacion({ isOpen, onClose, onAceptar }) {
    const handleAceptar = () => {
        if (onAceptar) {
            onAceptar();
        }
        onClose();
    };

    return (
        <Modal isOpen={isOpen} onClose={onClose} title="Cierre Cancelado">
            <div className="modal-cancelacion-content">
                <div className="cancelacion-icono">
                    <span className="icono-cancelar">✕</span>
                </div>
                <div className="cancelacion-mensaje">
                    <h3>Cierre Cancelado</h3>
                    <p>El proceso de cierre de orden ha sido cancelado. Todos los cambios han sido descartados.</p>
                </div>
                <div className="modal-cancelacion-actions">
                    <button 
                        className="btn-aceptar" 
                        onClick={handleAceptar}
                    >
                        Aceptar
                    </button>
                </div>
            </div>
        </Modal>
    );
}

export default ModalCancelacion;


