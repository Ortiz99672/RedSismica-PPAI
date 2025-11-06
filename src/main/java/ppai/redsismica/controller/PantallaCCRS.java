package ppai.redsismica.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import ppai.redsismica.service.GestorCierre;
import ppai.redsismica.dto.EmpleadoDTO;
import ppai.redsismica.dto.OrdenInspeccionDTO;
import ppai.redsismica.dto.DatosInicialesDTO;
import ppai.redsismica.dto.MotivoTipoDTO;
import java.util.List;

@RestController
@RequestMapping("/api/cierre-orden")
public class PantallaCCRS {

    // El controlador DEPENDE del Gestor (que tiene scope de sesión)
    private final GestorCierre gestorCierre;

    @Autowired
    public PantallaCCRS(GestorCierre gestorCierre) {
        this.gestorCierre = gestorCierre;
    }

    /**
     * PASO 1.1: habilitarVentana()
     * Este método DESAPARECE del backend.
     * La responsabilidad de "habilitar la ventana" es 100% de React Router
     * al cargar el componente <PantallaCierre> en el frontend.
     */
    /*

    /**
     * PASO 1.2: opcionCierreDeInscripcion()
     * El frontend (React en un useEffect) llama a este método APENAS se carga.
     * Este método le pasa la responsabilidad al Gestor
     * y devuelve los datos iniciales (el RI Logueado).
     *
     * Cambiado a GET ya que es para "obtener" datos iniciales.
     */
    @GetMapping("/cargar-datos-iniciales")
    public DatosInicialesDTO opcionCierreDeInscripcion() {
        System.out.println("PantallaCCRS: opcionCierreDeInscripcion() (llamado desde React)");

        // 1. Llama al gestor (que hace 1.2.1 y 1.2.3)
        gestorCierre.opcionCierreDeInscripcion();

        // 2. Le pide al gestor los resultados
        EmpleadoDTO riLogueado = gestorCierre.getResponsableInspeccionLogueado();
        List<OrdenInspeccionDTO> ordenes = gestorCierre.getOrdenesParaMostrar();

        // 3. Devuelve el DTO agrupado como JSON
        return new DatosInicialesDTO(riLogueado, ordenes);
    }

    @PostMapping("/seleccionar-orden")
    public ResponseEntity<Void> tomarSeleccionOrden(@RequestParam Integer nroOrden) {
        System.out.println("PantallaCCRS: Recibido tomarSeleccionOrden() para Nro: " + nroOrden);

        // 3.1: Llama al gestor para que guarde la selección en su estado
        gestorCierre.tomarSeleccionOrden(nroOrden);

        // Devolvemos un 200 OK vacío para confirmar la acción
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tomar-observacion")
    public List<MotivoTipoDTO> tomarObservacion(@RequestBody String observacion) {
        System.out.println("PantallaCCRS: Recibido tomarObservacion(): " + observacion);

        // 4.1.1: Llama al gestor
        // El gestor guardará la observación y ejecutará 4.1.2 (buscarMotivos)
        List<MotivoTipoDTO> motivos = gestorCierre.tomarObservacion(observacion);

        // 4.1.3: Devolvemos la lista de motivos al frontend como JSON
        return motivos;
    }

    @PostMapping("/seleccionar-motivo")
    public ResponseEntity<Void> tomarSeleccionMotivoFueraServicio(@RequestBody String motivoDescripcion) {
        System.out.println("PantallaCCRS: Recibido 5 tomarSeleccionMotivoFueraServicio(): " + motivoDescripcion);

        // 5.1: Llama al gestor para que guarde el motivo temporalmente
        gestorCierre.tomarSeleccionMotivoFueraDeServicio(motivoDescripcion);

        // 5.1.1: (solicitarIngresoComentario)
        // Devolvemos 200 OK. React interpreta esto como "éxito,
        // ahora habilita el campo de comentario".
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tomar-comentario")
    public ResponseEntity<Void> tomarIngresoComentario(@RequestBody String comentario) {
        System.out.println("PantallaCCRS: Recibido 6 tomarIngresoComentario(): " + comentario);

        // 6.1: Llama al gestor para que asocie el comentario
        // con el motivo temporal guardado en el paso 5.
        // (Usamos el método 'pasamanos' 6, que llama al 6.1)
        gestorCierre.tomarComentarioIngresado(comentario);

        // Devolvemos 200 OK. React puede ahora limpiar
        // los campos y prepararse para el siguiente loop.
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirmar-cierre")
    public ResponseEntity<String> tomarConfirmacion(@RequestBody boolean confirmacion) {
        System.out.println("PantallaCCRS: Recibido 7 tomarConfirmacion(): " + confirmacion);

        // 7.1: Llama al gestor
        boolean esValido = gestorCierre.tomarConfirmacionCierreOrden(confirmacion);

        if (esValido) {
            // Si la validación (7.1.1) fue exitosa
            return ResponseEntity.ok("Orden cerrada exitosamente.");
        } else {
            // Si la validación (7.1.1) falló
            // (o si el usuario envió 'false')
            if (!confirmacion) {
                return ResponseEntity.ok("Cierre cancelado por el usuario.");
            }
            return ResponseEntity
                    .badRequest()
                    .body("Datos insuficientes: Se requiere una observación y al menos un motivo con comentario.");
        }
    }


}