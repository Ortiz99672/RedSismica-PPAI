package ppai.redsismica.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

// Importaciones de Entidades
import ppai.redsismica.entity.Empleado;
import ppai.redsismica.entity.Rol;
import ppai.redsismica.entity.Sesion;
import ppai.redsismica.entity.Usuario;

// Importaciones de DTOs
import ppai.redsismica.dto.EmpleadoDTO;
import ppai.redsismica.dto.RolDTO;

// Importaciones de Repositorios
import ppai.redsismica.repository.SesionRepository;
// Asumo que tienes los otros repositorios y entidades en sus paquetes
// import ppai.redsismica.repository.EmpleadoRepository;
// import ppai.redsismica.repository.RolRepository;
// import ppai.redsismica.model.Orden;
// import ppai.redsismica.model.MotivoFS;
// import ppai.redsismica.model.Estado;
// import ppai.redsismica.model.Sismografo;
// import ppai.redsismica.model.Monitor;


@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GestorCierre {

    // --- 1. Dependencias (Inyectadas por Spring) ---
    private final SesionRepository sesionRepository;
    // private final EmpleadoRepository empleadoRepository; // Necesarios si Empleado/Rol no son EAGER
    // private final RolRepository rolRepository;
    // private final OrdenRepository ordenRepository;
    // private final SismografoRepository sismografoRepository;
    // private final NotificacionService notificacionService;

    // --- 2. Atributos de Estado (del diagrama) ---
    private EmpleadoDTO responsableInspeccionLogueado;

    // private List<Orden> ordenes;
    // ... (resto de atributos)


    /**
     * El constructor recibe las dependencias de Spring.
     */
    @Autowired
    public GestorCierre(SesionRepository sesionRepository
            /* , EmpleadoRepository empleadoRepo, RolRepository rolRepo ... */ ) {
        Sesion sesionActual = sesionRepository.findFirstByFechaHoraHastaIsNull();
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

        // 1.2.1: Llama al método wrapper
        this.buscarRILogueado();

        // 1.2.3: Llama al siguiente método de la secuencia
        this.buscarOrdenes();
    }

    /**
     * 1.2.1: Método wrapper para buscar el RI (Empleado) logueado.
     * Delega las llamadas a Sesion y Usuario.
     */
    public void buscarRILogueado() {
        System.out.println("GestorCierre: Ejecutando buscarRILogueado()...");

        if (sesionActual != null) {

            // 2. (1.2.2) El gestor le pide el usuario a la sesión ("obtenerUsuario")
            Usuario usuarioLogueado = sesionActual.getUsuario();

            if (usuarioLogueado != null) {

                // 3. (1.2.2.1) El gestor le pide el empleado al usuario ("getRILogueado")
                Empleado empleadoLogueado = usuarioLogueado.getEmpleado();

                if (empleadoLogueado != null) {
                    // 4. Mapeamos la Entidad a DTO y la guardamos en el estado del Gestor
                    this.responsableInspeccionLogueado = mapearEmpleadoADTO(empleadoLogueado);
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
     * 1.2.3: Siguiente paso en la secuencia (aún vacío).
     */
    public void buscarOrdenes() {
        System.out.println("GestorCierre: Ejecutando buscarOrdenes()...");
        // Lógica futura para buscar órdenes...
        // Setea 'this.ordenes'
    }

    public void ordenarOrdenes() {
        System.out.println("Ejecutando ordenarOrdenes...");
    }

    public void tomarSeleccionOrden(Long idOrden) {
        System.out.println("Ejecutando tomarSeleccionOrden...");
    }

    public void tomarObservacion(String observacion) {
        System.out.println("Ejecutando tomarObservacion...");
        this.observaciones = observacion;
    }

    public void buscarMotivoFS() {
        System.out.println("Ejecutando buscarMotivoFS...");
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


    // --- Métodos Helper (Privados) ---

    /**
     * Convierte la entidad Empleado a EmpleadoDTO.
     */
    private EmpleadoDTO mapearEmpleadoADTO(Empleado entidad) {
        if (entidad == null) return null;

        RolDTO rolDTO = null;
        if (entidad.getRol() != null) {
            Rol rolEntidad = entidad.getRol(); // Asumimos carga EAGER o sesión abierta
            rolDTO = new RolDTO(rolEntidad.getNombre(), rolEntidad.getDescripcion());
        }

        return new EmpleadoDTO(
                entidad.getMail(),
                entidad.getApellido(),
                entidad.getNombre(),
                entidad.getTelefono(),
                rolDTO
        );
    }
}