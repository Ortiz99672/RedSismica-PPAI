package ppai.redsismica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ppai.redsismica.entity.Sesion;

/**
 * Repositorio para la entidad Sesion.
 * La clave primaria (Id) es un String (el nombre de usuario).
 */
@Repository
public interface SesionRepository extends JpaRepository<Sesion, String> {

    /**
     * Busca la primera sesión que no tiene fechaHoraHasta (es decir, la sesión activa).
     * En un sistema de un solo usuario, esto devolverá la única sesión activa.
     */
    Sesion findFirstByFechaHoraHastaIsNull();
}