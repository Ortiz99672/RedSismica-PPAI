package ppai.redsismica.controller;

import ppai.redsismica.dto.NotificacionDTO;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

public interface ISujeto {
    void suscribir(IObservador observador);
    void quitar(IObservador observador);
    void notificar();
}
