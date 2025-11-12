import React, { useState, useEffect, useCallback } from 'react';
import observerService from '../services/ObserverService';
import ModalCierreInspeccion from '../components/ModalCierreInspeccion';
import ModalCancelacion from '../components/ModalCancelacion';

// --- Estilos CSS ---
// Para una app más grande, esto iría en un archivo .css separado.
const styles = `
    :root {
        --primary-color: #007bff;
        --secondary-color: #6c757d;
        --success-color: #28a745;
        --danger-color: #dc3545;
        --light-color: #f8f9fa;
        --dark-color: #343a40;
        --white-color: #fff;
        --border-color: #dee2e6;
        --shadow: 0 4px 8px rgba(0,0,0,0.1);
    }
    
    body {
        margin: 0;
        background-image: url('https://png.pngtree.com/background/20230614/original/pngtree-3d-image-of-waves-and-waves-on-a-black-background-picture-image_3502212.jpg');
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
        background-attachment: fixed;
        color: var(--dark-color);
        min-height: 100vh;
    }

    .cierre-container {
        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
        max-width: 900px;
        margin: 2rem auto;
        padding: 1rem;
        background-color: rgba(248, 249, 250, 0.95);
        border-radius: 8px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
        backdrop-filter: blur(5px);
    }

    .cierre-header {
        text-align: center;
        border-bottom: 1px solid var(--border-color);
        padding-bottom: 1rem;
        margin-bottom: 1.5rem;
    }

    .cierre-header h1 {
        color: var(--dark-color);
        margin-bottom: 0.5rem;
    }

    .api-status {
        padding: 1rem 1.5rem;
        border-radius: 4px;
        margin-bottom: 1.5rem;
        text-align: center;
        font-weight: 600;
        font-size: 1.1rem;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        animation: slideDown 0.3s ease;
        position: relative;
        z-index: 100;
    }

    @keyframes slideDown {
        from {
            opacity: 0;
            transform: translateY(-10px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    .api-status.success {
        background-color: #d4edda;
        color: #155724;
        border: 2px solid #28a745;
    }

    .api-status.error {
        background-color: #f8d7da;
        color: #721c24;
    }

    .step-card {
        background-color: var(--white-color);
        border: 1px solid var(--border-color);
        border-radius: 8px;
        padding: 1.5rem;
        margin-bottom: 1.5rem;
        box-shadow: var(--shadow);
        transition: opacity 0.3s ease, max-height 0.3s ease, margin 0.3s ease, padding 0.3s ease;
        opacity: 1;
        max-height: 2000px;
        overflow: hidden;
    }

    .step-card.disabled {
        opacity: 0;
        max-height: 0;
        margin-bottom: 0;
        padding-top: 0;
        padding-bottom: 0;
        pointer-events: none;
    }

    .step-card h2 {
        color: var(--primary-color);
        margin-top: 0;
        border-bottom: 1px solid var(--border-color);
        padding-bottom: 0.5rem;
        margin-bottom: 1rem;
    }

    .item-list {
        list-style: none;
        padding: 0;
    }

    .item-list li {
        display: flex;
        justify-content: space-between;
        align-items: center;
        background-color: #fff;
        border-bottom: 1px solid #eee;
        padding: 15px;
        transition: background-color 0.2s;
    }

    .item-list li .orden-numero {
        text-align: left;
        font-weight: bold;
        min-width: 120px; /* Ancho mínimo para que no se comprima */
    }

    .item-list li .orden-datos {
        text-align: center;
        flex-grow: 1; /* Ocupa el espacio central */
        font-size: 1rem;
        margin-left: 1rem;
        margin-right: 2rem;
        color: #555;
    }

    .item-list li span {
        flex-grow: 1;
        text-align: center;
        font-size: 1rem;
    }

    
    .item-list li.selected {
        background-color: #e7f3ff;
        font-weight: bold;
    }

    .item-list li:last-child {
        border-bottom: none;
    }

    button {
        padding: 0.5rem 1rem;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-weight: bold;
        transition: background-color 0.2s ease;
    }

    button:disabled {
        cursor: not-allowed;
        opacity: 0.6;
    }

    .btn-primary {
        background-color: var(--primary-color);
        color: var(--white-color);
    }
    .btn-primary:hover:not(:disabled) {
        background-color: #0056b3;
    }

    .btn-secondary {
        background-color: var(--secondary-color);
        color: var(--white-color);
    }
    .btn-secondary:hover:not(:disabled) {
        background-color: #545b62;
    }
    
    .btn-success {
        background-color: var(--success-color);
        color: var(--white-color);
    }
    .btn-danger {
        background-color: var(--danger-color);
        color: var(--white-color);
    }

    textarea {
        width: 100%;
        padding: 0.5rem;
        border: 1px solid var(--border-color);
        border-radius: 4px;
        min-height: 80px;
        margin-bottom: 1rem;
        box-sizing: border-box; /* Asegura que el padding no afecte el ancho */
        background-color: var(--white-color);
        color: var(--dark-color);
        font-family: inherit; /* Usa la misma fuente que el resto de la página */
        font-size: 1rem;
    }

    .button-group {
        display: flex;
        gap: 1rem;
        justify-content: flex-end;
    }

    .notificacion-card {
        background-color: #e7f3ff;
        border: 1px solid var(--primary-color);
        padding: 1.5rem;
        border-radius: 8px;
    }
    .notificacion-card h3 {
        color: var(--primary-color);
        margin-top: 0;
    }
    .notificacion-card pre {
        background-color: var(--white-color);
        padding: 1rem;
        border-radius: 4px;
        white-space: pre-wrap;
        word-wrap: break-word;
    }
`;

const API_BASE_URL = '/api/cierre-orden';

function PantallaCierre() {
    // Estados para los datos del backend
    const [responsable, setResponsable] = useState(null);
    const [ordenes, setOrdenes] = useState([]);
    const [motivos, setMotivos] = useState([]);
    const [notificacion, setNotificacion] = useState(null);

    // Estados para la entrada del usuario
    const [ordenSeleccionada, setOrdenSeleccionada] = useState(null);
    const [observacion, setObservacion] = useState('');
    const [motivoSeleccionado, setMotivoSeleccionado] = useState('');
    const [comentario, setComentario] = useState('');
    const [motivosAgregados, setMotivosAgregados] = useState({});

    // Estados para la UI
    const [loading, setLoading] = useState(false);
    const [apiMessage, setApiMessage] = useState({ text: '', isError: false });
    const [modalAbierto, setModalAbierto] = useState(false);
    const [modalCancelacionAbierto, setModalCancelacionAbierto] = useState(false);

    // Callback para recibir notificaciones del Observer Service
    const manejarNotificacion = useCallback((datosNotificacion) => {
        console.log('PantallaCierre: Recibida notificación del Observer Service:', datosNotificacion);
        if (datosNotificacion) {
            setNotificacion(datosNotificacion);
            // Primero mostrar mensaje de éxito, luego abrir modal después de un breve delay
            setApiMessage({ 
                text: '¡Cierre de orden exitoso! El sismógrafo ha sido actualizado y se han enviado los correos.', 
                isError: false 
            });
            // Abrir el modal después de 2 segundos para que el usuario vea claramente el mensaje de éxito
            setTimeout(() => {
                setModalAbierto(true);
            }, 2000);
        }
    }, []);

    // Suscribirse al Observer Service cuando el componente se monte
    useEffect(() => {
        console.log('PantallaCierre: Suscribiéndose al Observer Service...');
        const desuscribir = observerService.suscribir(manejarNotificacion);

        // Limpiar la suscripción cuando el componente se desmonte
        return () => {
            console.log('PantallaCierre: Desuscribiéndose del Observer Service...');
            desuscribir();
        };
    }, [manejarNotificacion]);

    // Función para cargar datos iniciales
    const cargarDatosIniciales = async () => {
        setLoading(true);
        try {
            const response = await fetch(`${API_BASE_URL}/cargar-datos-iniciales`);
            if (!response.ok) throw new Error('Error al cargar datos iniciales');
            const data = await response.json();
            setResponsable(data.riLogueado);
            setOrdenes(data.ordenes);
            setApiMessage({ text: 'Datos iniciales cargados. Por favor, seleccione una orden.', isError: false });
        } catch (error) {
            setApiMessage({ text: `Error: ${error.message}`, isError: true });
        } finally {
            setLoading(false);
        }
    };

    // 1. Cargar datos iniciales al montar el componente
    useEffect(() => {
        cargarDatosIniciales();
    }, []);

    // Función para volver atrás y reiniciar el proceso
    const handleVolverAtras = () => {
        // Limpiar todos los estados
        setNotificacion(null);
        setModalAbierto(false);
        setOrdenSeleccionada(null);
        setObservacion('');
        setMotivoSeleccionado('');
        setComentario('');
        setMotivosAgregados({});
        setMotivos([]);
        setApiMessage({ text: '', isError: false });
        
        // Recargar datos iniciales
        cargarDatosIniciales();
    };

    // Función para cerrar el modal
    const handleCerrarModal = () => {
        setModalAbierto(false);
    };

    // Función para manejar la cancelación completa
    const handleCancelacionCompleta = () => {
        // Limpiar todos los estados
        setNotificacion(null);
        setModalCancelacionAbierto(false);
        setOrdenSeleccionada(null);
        setObservacion('');
        setMotivoSeleccionado('');
        setComentario('');
        setMotivosAgregados({});
        setMotivos([]);
        setApiMessage({ text: '', isError: false });
        
        // Recargar datos iniciales
        cargarDatosIniciales();
    };

    const handleApiCall = async (url, options, successMessage) => {
        setLoading(true);
        try {
            const response = await fetch(url, options);
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Ocurrió un error en la solicitud.');
            }
            setApiMessage({ text: successMessage, isError: false });
            return await response.json().catch(() => ({})); // Devuelve JSON o un objeto vacío si no hay cuerpo
        } catch (error) {
            setApiMessage({ text: `Error: ${error.message}`, isError: true });
        } finally {
            setLoading(false);
        }
    };

    const handleSeleccionarOrden = async (nroOrden) => {
        await handleApiCall(
            `${API_BASE_URL}/seleccionar-orden?nroOrden=${nroOrden}`,
            { method: 'POST' },
            `Orden ${nroOrden} seleccionada. Ingrese una observación.`
        );
        setOrdenSeleccionada(nroOrden);
    };

    const handleTomarObservacion = async () => {
        const data = await handleApiCall(
            `${API_BASE_URL}/tomar-observacion`,
            { method: 'POST', headers: { 'Content-Type': 'text/plain' }, body: observacion },
            'Observación enviada. Ahora puede seleccionar motivos.'
        );
        if (data) setMotivos(data);
    };


    const handleSeleccionarMotivo = async (descripcionMotivo) => {
        await handleApiCall(
            `${API_BASE_URL}/seleccionar-motivo`,
            { method: 'POST', headers: { 'Content-Type': 'text/plain' }, body: descripcionMotivo },
            `Motivo "${descripcionMotivo}" seleccionado. Ingrese un comentario.`
        );
        setMotivoSeleccionado(descripcionMotivo);
    };

    const handleTomarComentario = async () => {
        await handleApiCall(
            `${API_BASE_URL}/tomar-comentario`,
            { method: 'POST', headers: { 'Content-Type': 'text/plain' }, body: comentario },
            `Comentario para "${motivoSeleccionado}" guardado. Puede agregar otro motivo.`
        );
        setMotivosAgregados(prev => ({ ...prev, [motivoSeleccionado]: comentario }));
        setComentario('');
        setMotivoSeleccionado('');
    };

    const handleConfirmarCierre = async (confirmacion) => {
        if (!confirmacion) {
            // Si se cancela, mostrar modal de cancelación
            setModalCancelacionAbierto(true);
            return;
        }

        const data = await handleApiCall(
            `${API_BASE_URL}/confirmar-cierre`,
            { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ confirmacion: confirmacion }) },
            '¡Cierre de orden exitoso! El sismógrafo ha sido actualizado y se han enviado los correos.'
        );
        
        if (data) {
            // El backend ya notificó a sus observadores (PantallaCCRS e InterfazNotificaciones)
            // Ahora notificamos a los observadores del frontend usando el patrón Observer
            console.log('PantallaCierre: Notificando a los observadores del frontend...');
            
            // Mapear los datos del backend al formato esperado por el frontend
            // El backend devuelve: identificadorSismografo, nombreEstado, fechaHora, motivos, destinatarios
            const notificacionFrontend = {
                idSismografo: data.identificadorSismografo,
                estadoSismografo: data.nombreEstado,
                fechaHora: data.fechaHora,
                motivos: data.motivos || {},
                mailsResponsables: data.destinatarios || []
            };
            
            // Notificar a todos los observadores suscritos en el frontend
            observerService.notificar(notificacionFrontend);
            
            // También actualizamos el estado local directamente
            setNotificacion(notificacionFrontend);
            
            // Mostrar mensaje de éxito primero
            setApiMessage({ 
                text: '¡Cierre de orden exitoso! El sismógrafo ha sido actualizado y se han enviado los correos.', 
                isError: false 
            });
            
            // Abrir el modal después de 2 segundos para que el usuario vea claramente el mensaje de éxito
            setTimeout(() => {
                setModalAbierto(true);
            }, 2000);
        }
    };

    return (
        <>
            <style>{styles}</style>
            <div className="cierre-container">
                <header className="cierre-header">
                    <h1>Cierre de Orden de Inspección</h1>
                    <p>Bienvenido <strong>Jacinto Barbosa</strong></p>
                </header>

                {apiMessage.text && (
                    <div className={`api-status ${apiMessage.isError ? 'error' : 'success'}`}>
                        {apiMessage.text}
                    </div>
                )}

                <ModalCierreInspeccion
                    isOpen={modalAbierto && notificacion !== null}
                    onClose={handleCerrarModal}
                    notificacion={notificacion}
                    onVolverAtras={handleVolverAtras}
                />

                <ModalCancelacion
                    isOpen={modalCancelacionAbierto}
                    onClose={() => setModalCancelacionAbierto(false)}
                    onAceptar={handleCancelacionCompleta}
                />

                {!notificacion && (
                    <>
                        <div className="step-card">
                            <h2>1. Seleccionar Orden de Inspección</h2>
                            <ul className="item-list">
                                {ordenes.map(orden => (
                                    <li key={orden.nroOrden} className={ordenSeleccionada === orden.nroOrden ? 'selected' : ''}>
                                        <span className="orden-numero">Orden N°{orden.nroOrden}</span>
                                        <span className="orden-datos">
                    Estado: {orden.estado.nombreEstado} - Fecha Finalización: {new Date(orden.fechaHoraFinalizacion).toLocaleString()}
                                            <br/>
                    Estación: {orden.nombreEstacion} - Sismógrafo: {orden.idSismografo}
                </span>
                                        <button className="btn-primary" onClick={() => handleSeleccionarOrden(orden.nroOrden)} disabled={loading}>
                                            Seleccionar
                                        </button>
                                    </li>
                                ))}
                            </ul>
                        </div>


                        <div className={`step-card ${!ordenSeleccionada ? 'disabled' : ''}`}>
                            <h2>2. Ingresar Observación de Cierre</h2>
                            <textarea value={observacion} onChange={(e) => setObservacion(e.target.value)} placeholder="Escriba su observación general sobre el cierre de la orden..." />
                            <div className="button-group">
                                <button className="btn-primary" onClick={handleTomarObservacion} disabled={loading || !observacion}>
                                    Enviar Observación
                                </button>
                            </div>
                        </div>

                        <div className={`step-card ${motivos.length === 0 ? 'disabled' : ''}`}>
                            <h2>3. Agregar Motivos de Fuera de Servicio</h2>
                            <ul className="item-list">
                                {motivos.map(motivo => (
                                    <li key={motivo.descripcion}>
                                        <span>{motivo.descripcion}</span>
                                        <button className="btn-secondary" onClick={() => handleSeleccionarMotivo(motivo.descripcion)} disabled={loading || !!motivoSeleccionado}>
                                            Elegir
                                        </button>
                                    </li>
                                ))}
                            </ul>
                            {motivoSeleccionado && (
                                <div style={{ marginTop: '1rem' }}>
                                    <h4>Comentario para "{motivoSeleccionado}"</h4>
                                    <textarea value={comentario} onChange={(e) => setComentario(e.target.value)} placeholder="Detalle el comentario para el motivo seleccionado..." />
                                    <div className="button-group">
                                        <button className="btn-primary" onClick={handleTomarComentario} disabled={loading || !comentario}>
                                            Agregar Comentario
                                        </button>
                                    </div>
                                </div>
                            )}
                            {Object.keys(motivosAgregados).length > 0 && (
                                <div style={{marginTop: '1.5rem'}}>
                                    <h4>Motivos Agregados:</h4>
                                    <ul>
                                        {Object.entries(motivosAgregados).map(([motivo, comment]) => (
                                            <li key={motivo}><strong>{motivo}:</strong> {comment}</li>
                                        ))}
                                    </ul>
                                </div>
                            )}
                        </div>

                        <div className={`step-card ${Object.keys(motivosAgregados).length === 0 ? 'disabled' : ''}`}>
                            <h2>4. Confirmar Cierre</h2>
                            <p>¿Confirma el cierre de la orden con los datos ingresados?</p>
                            <div className="button-group">
                                <button className="btn-danger" onClick={() => handleConfirmarCierre(false)} disabled={loading}>
                                    Cancelar
                                </button>
                                <button className="btn-success" onClick={() => handleConfirmarCierre(true)} disabled={loading}>
                                    Confirmar Cierre
                                </button>
                            </div>
                        </div>
                    </>
                )}
            </div>
        </>
    );
}

export default PantallaCierre;
