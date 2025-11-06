package ppai.redsismica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ppai.redsismica.entity.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, String> {
    // Para buscar estados por nombre y ámbito
    Estado findByNombreEstadoAndAmbito(String nombreEstado, String ambito);
}
