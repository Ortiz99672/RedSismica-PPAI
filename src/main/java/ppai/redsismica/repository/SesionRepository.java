package ppai.redsismica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ppai.redsismica.entity.Sesion;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, String> {

    /**
     * Busca una sesión que esté activa (es decir, que no tenga fecha de fin).
     * En un sistema de un solo usuario, esto debería devolver la sesión actual.
     */
    Sesion findFirstByFechaHoraHastaIsNull();
}
