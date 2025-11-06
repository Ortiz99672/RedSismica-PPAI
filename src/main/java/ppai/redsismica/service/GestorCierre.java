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
import ppai.redsismica.entity.MotivoTipo;

// Importaciones de Entidades
import ppai.redsismica.entity.Empleado;
import ppai.redsismica.entity.Rol;
import ppai.redsismica.entity.Sesion;
import ppai.redsismica.entity.Usuario;
import ppai.redsismica.entity.OrdenInspeccion;

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

    public void tomarSeleccionMotivoFueraDeServicio(Long idMotivo) {
        System.out.println("Ejecutando tomarSeleccionMotivoFueraDeServicio...");
    }

    public void tomarIngresoComentario(String comentario) {
        System.out.println("Ejecutando tomarIngresoComentario...");
        this.comentarios = comentario;
    }

    public void tomarConfirmacionCierreOrden(boolean confirmacion) {
        System.out.println("Ejecutando tomarConfirmacionCierreOrden...");
        this.confirmacionCierreOrden = confirmacion;
    }

    public boolean validarDatosMinimosRequeridosParaCierreMotivos() {
        System.out.println("Ejecutando validarDatosMinimosRequeridosParaCierreMotivos...");
        return false;
    }

    public Estado buscarEstadoCerradaParaOI() {
        System.out.println("Ejecutando buscarEstadoCerradaParaOI...");
        return null;
    }

    public Estado buscarEstadoSismografoFueraDeServicio() {
        System.out.println("Ejecutando buscarEstadoSismografoFueraDeServicio...");
        return null;
    }

    public void obtenerFechaYHoraActual() {
        System.out.println("Ejecutando obtenerFechaYHoraActual...");
        this.fechaYHoraActual = LocalDateTime.now();
    }

    public void cerrarOrdenInspeccion() {
        System.out.println("Ejecutando cerrarOrdenInspeccion...");
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