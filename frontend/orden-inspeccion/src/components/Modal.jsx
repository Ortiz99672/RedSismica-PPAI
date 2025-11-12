import React from 'react';
import './Modal.css';

/**
 * Componente Modal reutilizable
 * @param {boolean} isOpen - Controla si el modal está abierto
 * @param {function} onClose - Función que se ejecuta al cerrar el modal
 * @param {ReactNode} children - Contenido del modal
 * @param {string} title - Título del modal (opcional)
 */
function Modal({ isOpen, onClose, children, title }) {
    if (!isOpen) return null;

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                {title && (
                    <div className="modal-header">
                        <h2>{title}</h2>
                        <button className="modal-close-btn" onClick={onClose} aria-label="Cerrar">
                            ×
                        </button>
                    </div>
                )}
                <div className="modal-body">
                    {children}
                </div>
            </div>
        </div>
    );
}

export default Modal;


