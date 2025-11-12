package ppai.redsismica.controller;

import ppai.redsismica.dto.NotificacionDTO;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

public interface IObservador {
    void actualizar(NotificacionDTO notificacion, List<String> destinatarios);
}
