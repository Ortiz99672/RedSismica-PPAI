package ppai.redsismica.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ppai.redsismica.dto.DatosInicialesDTO;
import ppai.redsismica.dto.EmpleadoDTO;
import ppai.redsismica.dto.MotivoTipoDTO;
import ppai.redsismica.dto.NotificacionDTO;
import ppai.redsismica.dto.OrdenInspeccionDTO;
import ppai.redsismica.service.GestorCierre;

/**
 * Controlador REST que representa la Pantalla "Cierre de Orden de Inspección" (CCRS).
 * Actúa como un Observador del GestorCierre para recibir notificaciones
 * de cambios de estado y "publicarlos en monitores".
 */
@RestController
@RequestMapping("/api/cierre-orden")
public class PantallaCCRS implements IObservador {

    // El controlador depende del Gestor (que tiene scope de sesión)
    private final GestorCierre gestorCierre;

    // Atributos para almacenar los datos de la notificación para "mostrar en monitores"
    private String idSismografo;
    private String estado;
    private LocalDate fecha;
    private Map<String,String> motivos;

    @Autowired
    public PantallaCCRS(GestorCierre gestorCierre) {
        this.gestorCierre = gestorCierre;
    }

    // --- Getters y Setters de los datos de la Notificación ---

    public String getIdSismografo() {
        return idSismografo;
    }
    public void setIdSismografo(String idSismografo) {
        this.idSismografo = idSismografo;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public Map<String, String> getMotivos() {
        return motivos;
    }
    public void setMotivos(Map<String, String> motivos) {
        this.motivos = motivos;
    }

    // --- Métodos del Controlador (REST Endpoints) ---

    /**
     * Endpoint para iniciar el Caso de Uso (CU).
     * Llama al gestor para buscar el RI logueado y sus órdenes de inspección.
     * @return DTO con el RI Logueado y las órdenes para mostrar.
     */
    @GetMapping("/cargar-datos-iniciales")
    public DatosInicialesDTO opcionCierreDeInscripcion() {
        System.out.println("PantallaCCRS: opcionCierreDeInscripcion() (llamado desde React)");

        gestorCierre.opcionCierreDeInscripcion();

        EmpleadoDTO riLogueado = gestorCierre.getResponsableInspeccionLogueado();
        List<OrdenInspeccionDTO> ordenes = gestorCierre.getOrdenesParaMostrar();

        return new DatosInicialesDTO(riLogueado, ordenes);
    }

    /**
     * Endpoint para tomar la orden seleccionada por el usuario.
     * @param nroOrden Número de la orden seleccionada.
     */
    @PostMapping("/seleccionar-orden")
    public ResponseEntity<Void> tomarSeleccionOrden(@RequestParam Integer nroOrden) {
        System.out.println("PantallaCCRS: Recibido tomarSeleccionOrden() para Nro: " + nroOrden);

        gestorCierre.tomarSeleccionOrden(nroOrden);

        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para tomar la observación de cierre ingresada por el usuario.
     * @param observacion La observación ingresada.
     * @return Lista de motivos de fuera de servicio para seleccionar.
     */
    @PostMapping("/tomar-observacion")
    public List<MotivoTipoDTO> tomarObservacion(@RequestBody String observacion) {
        System.out.println("PantallaCCRS: Recibido tomarObservacion(): " + observacion);

        List<MotivoTipoDTO> motivos = gestorCierre.tomarObservacion(observacion);

        return motivos;
    }

    /**
     * Endpoint para tomar un motivo de fuera de servicio seleccionado.
     * @param motivoDescripcion Descripción del motivo seleccionado.
     */
    @PostMapping("/seleccionar-motivo")
    public ResponseEntity<Void> tomarSeleccionMotivoFueraServicio(@RequestBody String motivoDescripcion) {
        System.out.println("PantallaCCRS: Recibido tomarSeleccionMotivoFueraServicio(): " + motivoDescripcion);

        gestorCierre.tomarSeleccionMotivoFueraDeServicio(motivoDescripcion);

        // Se devuelve 200 OK para indicar que el frontend puede habilitar el campo de comentario.
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para tomar el comentario asociado al motivo temporal.
     * @param comentario El comentario ingresado.
     */
    @PostMapping("/tomar-comentario")
    public ResponseEntity<Void> tomarIngresoComentario(@RequestBody String comentario) {
        System.out.println("PantallaCCRS: Recibido tomarIngresoComentario(): " + comentario);

        gestorCierre.tomarComentarioIngresado(comentario);

        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint final para confirmar el cierre de la orden.
     * @param confirmacion true si el usuario confirma, false si cancela.
     * @return NotificacionDTO si el cierre es exitoso, o mensaje de error/cancelación.
     */
    @PostMapping("/confirmar-cierre")
    public ResponseEntity<?> tomarConfirmacion(@RequestBody boolean confirmacion) {
        System.out.println("PantallaCCRS: Recibido tomarConfirmacion(): " + confirmacion);

        NotificacionDTO notificacionDTO = gestorCierre.tomarConfirmacionCierreOrden(confirmacion);

        if (notificacionDTO != null) {
            // Cierre exitoso: el DTO se devuelve al frontend para confirmación visual
            return ResponseEntity.ok(notificacionDTO);

        } else {
            // Lógica de cancelación o error de validación
            if (!confirmacion) {
                return ResponseEntity.ok().body("Cierre cancelado por el usuario.");
            }
            // Error de validación de datos mínimos
            return ResponseEntity
                    .badRequest()
                    .body("Datos insuficientes: Se requiere una observacion y al menos un motivo con comentario.");
        }
    }

    // --- Implementación del Patrón Observer (IObservador) ---

    /**
     * Implementación del método de la interfaz IObservador.
     * Lógica para "publicar en monitores" (STUB).
     *
     * @param notificacion DTO con los detalles del sismógrafo.
     * @param destinatarios Lista de correos (no usada por la pantalla, pero requerida por la interfaz).
     */
    @Override
    public void actualizar(NotificacionDTO notificacion) { // ¡NUEVA FIRMA!
        // Almacenamos los datos para que el frontend los pueda mostrar.
        // ACTUALIZACION EN TODAS LAS PANTALLAS SIMULADA
        this.setIdSismografo(notificacion.getIdentificadorSismografo());
        this.setEstado(notificacion.getNombreEstado());
        this.setFecha(notificacion.getFechaHora().toLocalDate());
        this.setMotivos(notificacion.getMotivos());

        System.out.println(">>> (STUB) PantallaCCRS: Publicando actualización en monitores...");
        System.out.println("    Sismógrafo: " + notificacion.getIdentificadorSismografo());
        System.out.println("    Nuevo Estado: " + notificacion.getNombreEstado());
    }
}