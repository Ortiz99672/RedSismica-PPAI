package ppai.redsismica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ppai.redsismica.entity.OrdenInspeccion;

@Repository
public interface OrdenInspeccionRepository extends JpaRepository<OrdenInspeccion, Integer> {
    // Aquí puedes añadir métodos de búsqueda personalizados si los necesitas
}
