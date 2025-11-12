package ppai.redsismica.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import ppai.redsismica.controller.IObservador;
import ppai.redsismica.controller.ISujeto;
import ppai.redsismica.dto.EmpleadoDTO;
import ppai.redsismica.dto.MotivoTipoDTO;
import ppai.redsismica.dto.NotificacionDTO;
import ppai.redsismica.dto.OrdenInspeccionDTO;
import ppai.redsismica.entity.Empleado;
import ppai.redsismica.entity.Estado;
import ppai.redsismica.entity.MotivoTipo;
import ppai.redsismica.entity.OrdenInspeccion;
import ppai.redsismica.entity.Sesion;
import ppai.redsismica.entity.Usuario;
import ppai.redsismica.repository.EmpleadoRepository;
import ppai.redsismica.repository.EstadoRepository;
import ppai.redsismica.repository.MotivoTipoRepository;
import ppai.redsismica.repository.OrdenInspeccionRepository;
import ppai.redsismica.repository.SesionRepository;

/**
 * Clase que gestiona el Caso de Uso "Cerrar Orden de Inspección".
 * Tiene un scope de sesión y actúa como el **Sujeto** en el Patrón Observer
 * para notificar el cambio de estado del sismógrafo.
 */
@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GestorCierre implements ISujeto {

    // --- 1. Dependencias (Inyectadas por Spring) ---
    private final SesionRepository sesionRepository;
    private final OrdenInspeccionRepository ordenInspeccionRepository;
    private final EstadoRepository estadoRepository;
    private final MotivoTipoRepository motivoTipoRepository;
    private final EmpleadoRepository empleadoRepository;
    // Lista de observadores (Interfaces) inyectadas por Spring (PantallaCCRS, InterfazNotificaciones)
    private final List<IObservador> interfaces;


    // --- 2. Atributos de Estado del Gestor (Sesión) ---
    private Sesion sesionActual;
    private Empleado empleadoLogueado;
    private EmpleadoDTO responsableInspeccionLogueado;
    private List<OrdenInspeccionDTO> ordenesParaMostrar;
    private OrdenInspeccion ordenSeleccionada;
    private String observaciones;
    private List<MotivoTipo> motivosFueraServicio;
    private Map<String, String> motivosSeleccionadosConComentario = new HashMap<>();
    private MotivoTipo motivoTemporal;
    private boolean confirmacionCierreOrden;
    private boolean datosMinimosRequeridosParaCierre;
    private LocalDateTime fechaYHoraActual;
    private Estado estadoSismografoFueraDeServicio;
    private List<String> mailsResponsablesReparacion;
    private String idSismografo;

    // --- 3. Atributos del Patrón Observer ---
    private List<IObservador> observadores = new ArrayList<>();

    /**
     * Constructor que inyecta las dependencias del repositorio y las interfaces observadoras.
     */
    @Autowired
    public GestorCierre(SesionRepository sesionRepository, OrdenInspeccionRepository ordenInspeccionRepository,
                        EstadoRepository estadoRepository, MotivoTipoRepository motivoTipoRepository,
                        EmpleadoRepository empleadoRepository,
                        List<IObservador> interfaces) {
        this.sesionRepository = sesionRepository;
        this.ordenInspeccionRepository = ordenInspeccionRepository;
        this.estadoRepository = estadoRepository;
        this.motivoTipoRepository = motivoTipoRepository;
        this.empleadoRepository = empleadoRepository;
        this.interfaces = interfaces;

        // Carga la sesión activa al inicio del gestor
        this.sesionActual = sesionRepository.findFirstByFechaHoraHastaIsNull();
        if (this.sesionActual == null) {
            System.out.println("--- ADVERTENCIA: No se encontró sesión activa al crear GestorCierre ---");
        } else {
            System.out.println("--- GestorCierre creado para la sesión del usuario: " + this.sesionActual.getUsuario().getNombreUsuario() + " ---");
        }

        System.out.println("--- Se ha creado un NUEVO GestorCierre para la sesión ---");
    }

    // --- Métodos del Patrón Observer (ISujeto) ---

    @Override
    public void suscribir(IObservador observador) {
        System.out.println("GestorCierre: Suscribiendo observador: " + observador.getClass().getSimpleName());
        this.observadores.add(observador);
    }

    @Override
    public void quitar(IObservador observador) {
        System.out.println("GestorCierre: Quitando observador: " + observador.getClass().getSimpleName());
        this.observadores.remove(observador);
    }

    @Override
    public void notificar() {
        System.out.println("GestorCierre: Notificando a " + this.observadores.size() + " observadores...");

        // Se construye el DTO con los datos que se van a notificar Y los destinatarios
        NotificacionDTO notificacion = new NotificacionDTO(
                this.idSismografo,
                this.estadoSismografoFueraDeServicio.getNombreEstado(),
                this.fechaYHoraActual,
                this.motivosSeleccionadosConComentario,
                this.mailsResponsablesReparacion // ¡NUEVO ARGUMENTO!
        );

        // Llamar al método actualizar de cada observador (con la nueva firma)
        for (IObservador observador : this.observadores) {
            observador.actualizar(notificacion);
    }
}

    /**
     * Quita todos los observadores suscritos. Se usa al final del CU.
     */
    public void desuscribirTodos() {
        System.out.println("GestorCierre: Desuscribiendo todos los observadores...");
        this.observadores.clear();
    }

    // --- Lógica de Negocio (Diagrama de Secuencia) ---

    /**
     * Inicia el proceso de notificación. Se suscribe a los observadores
     * (Interfaces) y luego llama a `notificar()`.
     *
     * @return NotificacionDTO con los datos del sismógrafo.
     */
    public NotificacionDTO notificarSismografoFueraServicio() {
        
        this.obtenerMailResponsableReparacion();

        // Suscribe las interfaces inyectadas
        for (IObservador observador : this.interfaces) {
            this.suscribir(observador);
        }

        // Se notifica el cambio de estado (que internamente construye y usa el DTO con mails)
        this.notificar(); 

        // Se construye el DTO para ser devuelto al controlador (DEBE COINCIDIR)
        return new NotificacionDTO(
                this.idSismografo,
                this.estadoSismografoFueraDeServicio.getNombreEstado(),
                this.fechaYHoraActual,
                this.motivosSeleccionadosConComentario,
                this.mailsResponsablesReparacion // ¡NUEVO ARGUMENTO!
        );
    }

    /**
     * Busca los correos electrónicos de los empleados que son
     * responsables de reparación.
     */
    public void obtenerMailResponsableReparacion() {
        System.out.println("GestorCierre: Ejecutando obtenerMailResponsableReparacion()...");
        this.mailsResponsablesReparacion = new ArrayList<>();
        List<Empleado> todosLosEmpleados = empleadoRepository.findAll();

        for (Empleado empleado : todosLosEmpleados) {
            if (empleado.esResponsableDeReparacion()) {
                this.mailsResponsablesReparacion.add(empleado.obtenerMail());
            }
        }

        System.out.println("GestorCierre: Se encontraron " + this.mailsResponsablesReparacion.size() + " mails de responsables.");
    }

    /**
     * Inicia la secuencia de búsqueda de datos iniciales para la pantalla.
     */
    public void opcionCierreDeInscripcion() {
        System.out.println("GestorCierre: Recibida opcionCierreDeInscripcion()");
        this.buscarRILogueado();
        this.buscarOrdenes();
        this.ordenarOrdenes();
    }

    /**
     * Busca y asigna el Responsable de Inspección (RI) logueado.
     */
    public void buscarRILogueado() {
        System.out.println("GestorCierre: Ejecutando buscarRILogueado()...");
        if (this.sesionActual != null) {
            Usuario usuarioLogueado = this.sesionActual.getUsuario();
            if (usuarioLogueado != null) {
                Empleado empleadoEncontrado = usuarioLogueado.getEmpleado();
                if (empleadoEncontrado != null) {
                    this.empleadoLogueado = empleadoEncontrado;
                    this.responsableInspeccionLogueado = empleadoEncontrado.mapearADTO();
                    System.out.println("RI Logueado encontrado: " + this.responsableInspeccionLogueado.getNombre());
                } else {
                    System.out.println("Error: Usuario no tiene empleado asociado.");
                }
            } else {
                System.out.println("Error: Sesión no tiene usuario asociado.");
            }
        } else {
            System.out.println("Error: No se encontró sesión activa.");
        }
    }

    /**
     * Busca las órdenes de inspección que le corresponden al RI logueado
     * y que tienen estado "Completamente Realizado".
     */
    public void buscarOrdenes() {
        System.out.println("GestorCierre: Ejecutando buscarOrdenes()...");
        if (this.empleadoLogueado == null) {
            System.out.println("GestorCierre: No se puede buscar órdenes si no hay un empleado logueado.");
            this.ordenesParaMostrar = new ArrayList<>();
            return;
        }

        List<OrdenInspeccion> todasLasOrdenes = ordenInspeccionRepository.findAll();

        this.ordenesParaMostrar = todasLasOrdenes.stream()
                .filter(orden -> orden.esDeEmpleado(this.empleadoLogueado))
                .filter(orden -> orden.sosEstadoCompletamenteRealizado())
                .map(orden -> orden.mapearADTO())
                .collect(Collectors.toList());

        System.out.println("GestorCierre: Se encontraron " + this.ordenesParaMostrar.size() + " órdenes válidas para mostrar.");
    }

    /**
     * Ordena las órdenes encontradas por fecha de finalización (descendente).
     */
    public void ordenarOrdenes() {
        System.out.println("GestorCierre: Ejecutando ordenarOrdenes()...");
        if (this.ordenesParaMostrar != null) {
            this.ordenesParaMostrar.sort(Comparator.comparing(
                    OrdenInspeccionDTO::getFechaHoraFinalizacion,
                    Comparator.nullsLast(Comparator.reverseOrder())
            ));
        }
    }

    /**
     * Registra la orden de inspección seleccionada por el usuario en el estado del gestor.
     * @param nroOrden Número de la orden seleccionada.
     */
    public void tomarSeleccionOrden(Integer nroOrden) {
        System.out.println("GestorCierre: Ejecutando tomarSeleccionOrden() para Nro: " + nroOrden);
        this.ordenSeleccionada = ordenInspeccionRepository.findById(nroOrden).orElse(null);

        if (this.ordenSeleccionada == null) {
            System.out.println("Error: No se encontró la entidad Orden " + nroOrden);
        } else {
            System.out.println("Orden seleccionada: " + this.ordenSeleccionada.getNroOrden());
            this.idSismografo = this.ordenSeleccionada.getEstacionSismologica().getSismografo().getIdentificadorSismografo();
        }
    }

    /**
     * Registra la observación de cierre y dispara la búsqueda de motivos.
     * @param observacion La observación ingresada por el usuario.
     * @return Lista de DTOs de motivos de fuera de servicio.
     */
    public List<MotivoTipoDTO> tomarObservacion(String observacion) {
        System.out.println("GestorCierre: Ejecutando tomarObservacion(): " + observacion);
        this.observaciones = observacion;
        return this.buscarMotivoFS();
    }

    /**
     * Busca todos los Motivos de Fuera de Servicio.
     * @return Lista de DTOs de motivos.
     */
    public List<MotivoTipoDTO> buscarMotivoFS() {
        System.out.println("GestorCierre: Ejecutando buscarMotivoFS()...");

        this.motivosFueraServicio = motivoTipoRepository.findAll();

        List<MotivoTipoDTO> motivosDTO = this.motivosFueraServicio.stream()
                .map(motivo -> motivo.mapearADTO())
                .collect(Collectors.toList());

        System.out.println("Se encontraron " + motivosDTO.size() + " motivos.");

        return motivosDTO;
    }

    /**
     * Registra el motivo de fuera de servicio seleccionado temporalmente.
     * @param motivoDescripcion Descripción del motivo seleccionado.
     */
    public void tomarSeleccionMotivoFueraDeServicio(String motivoDescripcion) {
        System.out.println("GestorCierre: Ejecutando tomarSeleccionMotivoFueraServicio(): " + motivoDescripcion);

        this.motivoTemporal = this.motivosFueraServicio.stream()
                .filter(m -> m.getDescripcion().equals(motivoDescripcion))
                .findFirst()
                .orElse(null);

        if (this.motivoTemporal == null) {
            System.out.println("Error: El motivo '" + motivoDescripcion + "' no es válido o no se encontró.");
        } else {
            System.out.println("Motivo temporal guardado: " + this.motivoTemporal.getDescripcion());
        }
    }

    /**
     * Asocia el comentario ingresado con el motivo temporal y lo guarda.
     * @param comentario Comentario ingresado por el usuario.
     */
    public void tomarComentarioIngresado(String comentario) {
        System.out.println("GestorCierre: Ejecutando tomarComentarioIngresado(): " + comentario);

        if (this.motivoTemporal == null) {
            System.out.println("Error: Se intentó guardar un comentario sin un motivo seleccionado temporalmente.");
            return;
        }

        this.motivosSeleccionadosConComentario.put(this.motivoTemporal.getDescripcion(), comentario);

        System.out.println("GestorCierre: Motivo '" + this.motivoTemporal.getDescripcion() + "' y comentario guardados. Total: " + this.motivosSeleccionadosConComentario.size());

        // Limpiamos el motivo temporal
        this.motivoTemporal = null;
    }

    /**
     * Lógica principal del CU: Valida, cierra la orden, y notifica si corresponde.
     *
     * @param confirmacion true si el usuario confirma, false si cancela.
     * @return NotificacionDTO con los datos si el cierre fue exitoso, o null en caso contrario.
     */
    public NotificacionDTO tomarConfirmacionCierreOrden(boolean confirmacion) {
        System.out.println("GestorCierre: Ejecutando tomarConfirmacionCierreOrden()...");
        this.confirmacionCierreOrden = confirmacion;

        if (!this.confirmacionCierreOrden) {
            System.out.println("El usuario canceló el cierre.");
            this.finCU();
            return null;
        }

        boolean esValido = this.validarDatosMinimosRequeridosParaCierreMotivos();

        if (esValido) {
            System.out.println("GestorCierre: Validación exitosa.");
            Estado estadoCerrada = this.buscarEstadoCerradaParaOI();

            if (estadoCerrada != null) {
                this.estadoSismografoFueraDeServicio = this.buscarEstadoSismografoFueraDeServicio();
                this.obtenerFechaYHoraActual();
                this.cerrarOrdenInspeccion(estadoCerrada);

                if (this.estadoSismografoFueraDeServicio != null) {
                    this.enviarSismografoParaReparacion();
                }

                // Patrón Observer: Notificación a Observadores (Pantalla, InterfazNotificaciones)
                NotificacionDTO notificacionDTO = this.notificarSismografoFueraServicio();

                this.finCU();
                // Retorna el DTO para el controlador/frontend
                return notificacionDTO;

            } else {
                System.out.println("Error: No se pudo encontrar el estado 'Cerrada' para 'OrdenInspeccion'");
            }
        }
        // Devuelve null si la validación falla o no se encuentra el estado "Cerrada"
        return null;
    }

    /**
     * Valida la existencia de una observación y al menos un motivo seleccionado.
     * @return true si los datos mínimos son válidos.
     */
    public boolean validarDatosMinimosRequeridosParaCierreMotivos() {
        System.out.println("GestorCierre: Ejecutando validarDatosMinimosRequeridosParaCierreMotivos()...");

        boolean observacionValida = (this.observaciones != null && !this.observaciones.trim().isEmpty());
        boolean motivosValidos = (this.motivosSeleccionadosConComentario != null && !this.motivosSeleccionadosConComentario.isEmpty());

        if (!observacionValida) System.out.println("Validación fallida: No hay observación.");
        if (!motivosValidos) System.out.println("Validación fallida: No hay motivos seleccionados.");

        this.datosMinimosRequeridosParaCierre = (observacionValida && motivosValidos);
        return this.datosMinimosRequeridosParaCierre;
    }

    /**
     * Busca el estado "Cerrada" para el ámbito "OrdenInspeccion".
     * @return La entidad Estado "Cerrada" o null si no se encuentra.
     */
    public Estado buscarEstadoCerradaParaOI() {
        System.out.println("GestorCierre: Ejecutando buscarEstadoCerradaParaOI()...");
        List<Estado> todosLosEstados = estadoRepository.findAll();

        for (Estado estado : todosLosEstados) {
            if (estado.esAmbitoOI() && estado.esCerrada()) {
                System.out.println("Estado 'Cerrada' encontrado.");
                return estado;
            }
        }
        return null;
    }

    /**
     * Busca el estado "Fuera de Servicio" para el ámbito "Sismografo".
     * @return La entidad Estado "Fuera de Servicio" o null si no se encuentra.
     */
    public Estado buscarEstadoSismografoFueraDeServicio() {
        System.out.println("GestorCierre: Ejecutando buscarEstadoSismografoFueraDeServicio()...");
        List<Estado> todosLosEstados = estadoRepository.findAll();

        for (Estado estado : todosLosEstados) {
            if (estado.esAmbitoSismografo() && estado.esFueraDeServicio()) {
                System.out.println("Estado 'Fuera de Servicio' (Sismografo) encontrado.");
                return estado;
            }
        }

        System.out.println("Error: No se pudo encontrar el estado 'Fuera de Servicio' para 'Sismografo'");
        return null;
    }

    /**
     * Obtiene y registra la fecha y hora actual del sistema.
     */
    public void obtenerFechaYHoraActual() {
        System.out.println("GestorCierre: Ejecutando obtenerFechaYHoraActual()...");
        this.fechaYHoraActual = LocalDateTime.now();
    }

    /**
     * Cierra la orden de inspección seleccionada con el estado y la observación.
     * @param estadoCerrada La entidad Estado "Cerrada".
     */
    public void cerrarOrdenInspeccion(Estado estadoCerrada) {
        System.out.println("GestorCierre: Ejecutando cerrarOrdenInspeccion()...");

        if (this.ordenSeleccionada == null) {
            System.out.println("Error: No hay orden seleccionada para cerrar.");
            return;
        }

        this.ordenSeleccionada.cerrar(estadoCerrada, this.fechaYHoraActual, this.observaciones);
        ordenInspeccionRepository.save(this.ordenSeleccionada);

        System.out.println("Orden " + this.ordenSeleccionada.getNroOrden() + " guardada en estado 'Cerrada'.");
    }

    /**
     * Llama a la lógica de la Orden para cambiar el estado del Sismógrafo
     * a "Fuera de Servicio".
     */
    public void enviarSismografoParaReparacion() {
        System.out.println("GestorCierre: Ejecutando enviarSismografoParaReparacion()...");

        if (this.ordenSeleccionada == null || this.estadoSismografoFueraDeServicio == null || this.empleadoLogueado == null) {
            System.out.println("Error: Datos insuficientes para enviar sismógrafo a reparación.");
            return;
        }

        this.ordenSeleccionada.enviarSismografoParaReparacion(
                this.estadoSismografoFueraDeServicio,
                this.fechaYHoraActual,
                this.empleadoLogueado,
                this.motivosFueraServicio,
                this.motivosSeleccionadosConComentario,
                this.mailsResponsablesReparacion
        );
    }

    /**
     * Finaliza el Caso de Uso, limpiando el estado de la sesión.
     */
    public void finCU() {
        System.out.println("GestorCierre: Ejecutando finCU()...");

        // Desuscribe todos los observadores para limpiar la referencia
        this.desuscribirTodos();

        // Limpiamos los atributos de estado para el próximo caso de uso
        this.responsableInspeccionLogueado = null;
        this.empleadoLogueado = null;
        this.ordenesParaMostrar = null;
        this.ordenSeleccionada = null;
        this.observaciones = null;
        this.motivosFueraServicio = null;
        this.motivosSeleccionadosConComentario = new HashMap<>();
        this.motivoTemporal = null;
        this.confirmacionCierreOrden = false;
        this.datosMinimosRequeridosParaCierre = false;
        this.fechaYHoraActual = null;
        this.estadoSismografoFueraDeServicio = null;
        this.mailsResponsablesReparacion = null;
        this.idSismografo = null;

        System.out.println("GestorCierre: Estado de sesión limpiado. FIN CU.");
    }

    // --- Getters para el Controlador ---
    public EmpleadoDTO getResponsableInspeccionLogueado() {
        return this.responsableInspeccionLogueado;
    }

    public List<OrdenInspeccionDTO> getOrdenesParaMostrar() {
        return this.ordenesParaMostrar;
    }
}