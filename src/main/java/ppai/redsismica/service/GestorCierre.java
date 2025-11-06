package ppai.redsismica.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;


// Importaciones de Entidades
import ppai.redsismica.entity.Empleado;
import ppai.redsismica.entity.Sesion;
import ppai.redsismica.entity.Usuario;
import ppai.redsismica.entity.OrdenInspeccion;
import ppai.redsismica.entity.Estado;
import ppai.redsismica.entity.MotivoTipo;

// Importaciones de DTOs
import ppai.redsismica.dto.EmpleadoDTO;
import ppai.redsismica.dto.OrdenInspeccionDTO;
import ppai.redsismica.dto.MotivoTipoDTO;

// Importaciones de Repositorios
import ppai.redsismica.repository.SesionRepository;
import ppai.redsismica.repository.OrdenInspeccionRepository;
import ppai.redsismica.repository.EstadoRepository;
import ppai.redsismica.repository.MotivoTipoRepository;



@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GestorCierre {

    // --- 1. Dependencias (Inyectadas por Spring) ---
    private final SesionRepository sesionRepository;
    private final OrdenInspeccionRepository ordenInspeccionRepository;
    private final EstadoRepository estadoRepository;
    private final MotivoTipoRepository motivoTipoRepository;


    // --- 2. Atributos de Estado (del diagrama) ---
    private EmpleadoDTO responsableInspeccionLogueado;
    private Sesion sesionActual;
    private Empleado empleadoLogueado;
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



    /**
     * El constructor recibe las dependencias de Spring.
     */
    @Autowired
    public GestorCierre(SesionRepository sesionRepository, OrdenInspeccionRepository ordenInspeccionRepository,
                        EstadoRepository estadoRepository, MotivoTipoRepository motivoTipoRepository) {
        this.sesionRepository = sesionRepository;
        this.ordenInspeccionRepository = ordenInspeccionRepository;
        this.estadoRepository = estadoRepository;
        this.motivoTipoRepository = motivoTipoRepository;
        this.sesionActual = sesionRepository.findFirstByFechaHoraHastaIsNull();
        if (this.sesionActual == null) {
            System.out.println("--- ADVERTENCIA: No se encontró sesión activa al crear GestorCierre ---");
        } else {
            System.out.println("--- GestorCierre creado para la sesión del usuario: " + this.sesionActual.getUsuario().getNombreUsuario() + " ---");
        }

        // this.empleadoRepository = empleadoRepo;
        // this.rolRepository = rolRepo;
        System.out.println("--- Se ha creado un NUEVO GestorCierre para la sesión ---");
    }

    // --- 3. Métodos del Diagrama (Implementados) ---

    /**
     * 1.2: Metodo llamado por la PantallaCierre.
     * Es el "pasamanos" que inicia la secuencia.
     */
    public void opcionCierreDeInscripcion() {
        System.out.println("GestorCierre: Recibida opcionCierreDeInscripcion()");


        this.buscarRILogueado();
        this.buscarOrdenes();
        this.ordenarOrdenes();
    }

    /**
     * 1.2.1: Método wrapper para buscar el RI (Empleado) logueado.
     * Delega las llamadas a Sesion y Usuario.
     */
    public void buscarRILogueado() {
        System.out.println("GestorCierre: Ejecutando buscarRILogueado()...");

        // 1. Usamos la sesión que ya cargamos en el constructor
        if (this.sesionActual != null) {

            // 2. (1.2.2) El gestor le pide el usuario a la sesión ("obtenerUsuario")
            Usuario usuarioLogueado = this.sesionActual.getUsuario();

            if (usuarioLogueado != null) {

                // 3. (1.2.2.1) El gestor le pide el empleado al usuario ("getRILogueado")
                Empleado empleadoLogueado = usuarioLogueado.getEmpleado();

                if (empleadoLogueado != null) {
                    // --- CORRECCIÓN 2: Delegamos el mapeo a la entidad Empleado ---
                    this.responsableInspeccionLogueado = empleadoLogueado.mapearADTO(); // ¡Correcto!

                    System.out.println("RI Logueado encontrado: " + this.responsableInspeccionLogueado.getNombre());
                } else {
                    System.out.println("Error: Usuario no tiene empleado asociado.");
                }
            } else {
                System.out.println("Error: Sesión no tiene usuario asociado.");
            }
        } else {
            System.out.println("Error: No se encontró sesión activa (cargada en el constructor).");
        }
    }

    /**
     * 1.2.3: Implementa la búsqueda y filtrado de órdenes.
     */
    public void buscarOrdenes() {
        System.out.println("GestorCierre: Ejecutando buscarOrdenes()...");
        if (this.empleadoLogueado == null) {
            System.out.println("GestorCierre: No se puede buscar órdenes si no hay un empleado logueado.");
            this.ordenesParaMostrar = new ArrayList<>();
            return;
        }

        // 1. Buscamos TODAS las órdenes (se puede optimizar)
        List<OrdenInspeccion> todasLasOrdenes = ordenInspeccionRepository.findAll();

        // 2. Iniciamos el loop de filtrado
        this.ordenesParaMostrar = todasLasOrdenes.stream()
                // 1.2.4: esDeEmpleado(RI)
                .filter(orden -> orden.esDeEmpleado(this.empleadoLogueado))
                // 1.2.5: sosEstadoCompletamenteRealizado()
                .filter(orden -> orden.sosEstadoCompletamenteRealizado())
                // 1.2.6: mostrarDatosOrden() (convertimos a DTO)
                .map(orden -> orden.mapearADTO())
                .collect(Collectors.toList());

        System.out.println("GestorCierre: Se encontraron " + this.ordenesParaMostrar.size() + " órdenes válidas para mostrar.");
    }


    public void ordenarOrdenes() {
        System.out.println("GestorCierre: Ejecutando ordenarOrdenes()...");
        if (this.ordenesParaMostrar != null) {
            // Ordenamos por fecha de finalización, de más reciente a más antigua (descendente)
            this.ordenesParaMostrar.sort(Comparator.comparing(
                    OrdenInspeccionDTO::getFechaHoraFinalizacion,
                    Comparator.nullsLast(Comparator.reverseOrder()) // Maneja nulos y ordena descendente
            ));
        }
    }

    public void tomarSeleccionOrden(Integer nroOrden) {
        System.out.println("GestorCierre: Ejecutando tomarSeleccionOrden() para Nro: " + nroOrden);
        // Buscamos la *entidad* completa y la guardamos en estado
        this.ordenSeleccionada = ordenInspeccionRepository.findById(nroOrden).orElse(null);

        if (this.ordenSeleccionada == null) {
            System.out.println("Error: No se encontró la entidad Orden " + nroOrden);
        } else {
            System.out.println("Orden seleccionada: " + this.ordenSeleccionada.getNroOrden());
        }
    }

    /**
     * 4: El usuario ingresa la observación.
     * El controlador llama a este método.
     * Este método, además, inicia la búsqueda de Motivos (Paso 4.1.2)
     * y devuelve la lista de motivos al controlador.
     */
    public List<MotivoTipoDTO> tomarObservacion(String observacion) {
        System.out.println("GestorCierre: Ejecutando tomarObservacion(): " + observacion);
        this.observaciones = observacion;

        // El diagrama indica que "pedirObservacion" (4.1.1)
        // dispara "buscarMotivoFS" (4.1.2)
        return this.buscarMotivoFS();
    }

    /**
     * 4.1.2: Busca los Motivos de Fuera de Servicio.
     */
    public List<MotivoTipoDTO> buscarMotivoFS() {
        System.out.println("GestorCierre: Ejecutando buscarMotivoFS()...");

        // 1. Buscamos las entidades
        this.motivosFueraServicio = motivoTipoRepository.findAll();

        // 2. Mapeamos a DTO (4.1.2.1: getDescripcion es llamado por mapearADTO)
        List<MotivoTipoDTO> motivosDTO = this.motivosFueraServicio.stream()
                .map(motivo -> motivo.mapearADTO())
                .collect(Collectors.toList());

        System.out.println("Se encontraron " + motivosDTO.size() + " motivos.");

        // 3. Devolvemos la lista de DTOs para que el
        // controlador (Pantalla) se la muestre al usuario
        return motivosDTO;
    }

    public void tomarSeleccionMotivoFueraDeServicio(String motivoDescripcion) {
        System.out.println("GestorCierre: Ejecutando 5.1 tomarSeleccionMotivoFueraServicio(): " + motivoDescripcion);

        // Busca en la lista de motivos cargados
        this.motivoTemporal = this.motivosFueraServicio.stream()
                .filter(m -> m.getDescripcion().equals(motivoDescripcion))
                .findFirst()
                .orElse(null);

        if (this.motivoTemporal == null) {
            System.out.println("Error: El motivo '" + motivoDescripcion + "' no es válido o no se encontró.");
        } else {
            System.out.println("Motivo temporal guardado: " + this.motivoTemporal.getDescripcion());
            // 5.1.1: solicitarIngresoComentario() -> En React, esto es
            // la app habilitando el campo de texto para el comentario.
            // Aquí solo damos el OK implícito (status 200).
        }
    }

    public void tomarComentarioIngresado(String comentario) {
        System.out.println("GestorCierre: Ejecutando 6.1 tomarComentarioIngresado(): " + comentario);

        if (this.motivoTemporal == null) {
            System.out.println("Error: Se intentó guardar un comentario sin un motivo seleccionado temporalmente.");
            return;
        }

        // Asociamos el comentario con el motivo temporal y lo guardamos en el Map
        this.motivosSeleccionadosConComentario.put(this.motivoTemporal.getDescripcion(), comentario);

        System.out.println("GestorCierre: Motivo '" + this.motivoTemporal.getDescripcion() + "' y comentario guardados. Total: " + this.motivosSeleccionadosConComentario.size());

        // Limpiamos el motivo temporal, listos para el siguiente loop
        this.motivoTemporal = null;
    }

    public boolean tomarConfirmacionCierreOrden(boolean confirmacion) {
        System.out.println("GestorCierre: Ejecutando 7.1 tomarConfirmacionCierreOrden()...");
        this.confirmacionCierreOrden = confirmacion;

        if (!this.confirmacionCierreOrden) {
            System.out.println("El usuario canceló el cierre.");
            this.finCU();
            return false;
        }

        // 7.1.1: Llama a la validación
        boolean esValido = this.validarDatosMinimosRequeridosParaCierreMotivos();

        if (esValido) {
            System.out.println("GestorCierre: Validación exitosa.");

            // 7.1.2: Llama a buscar el estado "Cerrada"
            Estado estadoCerrada = this.buscarEstadoCerradaParaOI();

            if (estadoCerrada != null) {
                System.out.println("GestorCierre: Búsqueda de estado 'Cerrada' completada.");

                // AÑADIDO: (Paso 7.1.5)
                this.estadoSismografoFueraDeServicio = this.buscarEstadoSismografoFueraDeServicio();
                this.obtenerFechaYHoraActual();
                this.cerrarOrdenInspeccion(estadoCerrada);

            } else {
                System.out.println("Error: No se pudo encontrar el estado 'Cerrada' para 'OrdenInspeccion'");
            }
        }
        return esValido;
    }

    /**
     * 7.1.1: Valida los datos mínimos según la descripción del CU.
     */
    public boolean validarDatosMinimosRequeridosParaCierreMotivos() {
        System.out.println("GestorCierre: Ejecutando 7.1.1 validarDatosMinimosRequeridosParaCierreMotivos()...");

        // 1. "valida que exista una observación de cierre de orden"
        boolean observacionValida = (this.observaciones != null && !this.observaciones.trim().isEmpty());

        // 2. "y al menos un motivo seleccionado"
        boolean motivosValidos = (this.motivosSeleccionadosConComentario != null && !this.motivosSeleccionadosConComentario.isEmpty());

        if (!observacionValida) System.out.println("Validación fallida: No hay observación.");
        if (!motivosValidos) System.out.println("Validación fallida: No hay motivos seleccionados.");

        this.datosMinimosRequeridosParaCierre = (observacionValida && motivosValidos);
        return this.datosMinimosRequeridosParaCierre;
    }

    /**
     * 7.1.2: Busca el estado "Cerrada" para el ámbito "OrdenInspeccion".
     */
    public Estado buscarEstadoCerradaParaOI() {
        System.out.println("GestorCierre: Ejecutando 7.1.2 buscarEstadoCerradaParaOI()...");
        List<Estado> todosLosEstados = estadoRepository.findAll();

        // Loop (7.1.3 y 7.1.4)
        for (Estado estado : todosLosEstados) {
            if (estado.esAmbitoOI() && estado.esCerrada()) {
                System.out.println("Estado 'Cerrada' encontrado.");
                return estado;
            }
        }

        return null;
    }

    public Estado buscarEstadoSismografoFueraDeServicio() {
        System.out.println("GestorCierre: Ejecutando 7.1.5 buscarEstadoSismografoFueraDeServicio()...");
        List<Estado> todosLosEstados = estadoRepository.findAll();

        // Loop (7.1.6 y 7.1.7)
        for (Estado estado : todosLosEstados) {
            // 7.1.6: esAmbitoSismografo()
            // 7.1.7: esFueraDeServicio()
            if (estado.esAmbitoSismografo() && estado.esFueraDeServicio()) {
                System.out.println("Estado 'Fuera de Servicio' (Sismografo) encontrado.");
                return estado;
            }
        }

        System.out.println("Error: No se pudo encontrar el estado 'Fuera de Servicio' para 'Sismografo'");
        return null;
    }

    /**
     * 7.1.8: Implementación de "obtenerFechaYHoraActual"
     */
    public void obtenerFechaYHoraActual() {
        System.out.println("GestorCierre: Ejecutando 7.1.8 obtenerFechaYHoraActual()...");
        this.fechaYHoraActual = LocalDateTime.now();
    }

    /**
     * 7.1.9: Implementación de "cerrarOrdenInspeccion"
     */
    public void cerrarOrdenInspeccion(Estado estadoCerrada) {
        System.out.println("GestorCierre: Ejecutando 7.1.9 cerrarOrdenInspeccion()...");

        if (this.ordenSeleccionada == null) {
            System.out.println("Error: No hay orden seleccionada para cerrar.");
            return;
        }

        // 7.1.10: Delega el cierre a la propia orden
        this.ordenSeleccionada.cerrar(estadoCerrada, this.fechaYHoraActual, this.observaciones);

        // Guardamos en la base de datos
        ordenInspeccionRepository.save(this.ordenSeleccionada);

        System.out.println("Orden " + this.ordenSeleccionada.getNroOrden() + " guardada en estado 'Cerrada'.");
    }

    public void enviarSismografoParaReparacion() {
        System.out.println("Ejecutando enviarSismografoParaReparacion...");
    }

    public String obtenerMailResponsableReparacion() {
        System.out.println("Ejecutando obtenerMailResponsableReparacion...");
        return "ejemplo@reparacion.com";
    }

    public void publicarEnMonitores() {
        System.out.println("Ejecutando publicarEnMonitores...");
    }

    public void enviarNotificacionesPorMail() {
        System.out.println("Ejecutando enviarNotificacionesPorMail...");
    }

    public void finCU() {
        System.out.println("Ejecutando finCU...");
        // Opcionalmente: limpiar el estado de este gestor
        // para que la próxima vez que inicie el CU esté vacío.
        // this.ordenes = null;
        // this.observaciones = null;
    }

    // --- Getters para el Controlador ---
    public EmpleadoDTO getResponsableInspeccionLogueado() {
        return this.responsableInspeccionLogueado;
    }

    public List<OrdenInspeccionDTO> getOrdenesParaMostrar() {
        return this.ordenesParaMostrar;
    }




}