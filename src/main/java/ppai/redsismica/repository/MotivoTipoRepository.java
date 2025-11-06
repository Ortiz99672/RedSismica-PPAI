package ppai.redsismica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ppai.redsismica.entity.MotivoTipo;

@Repository
public interface MotivoTipoRepository extends JpaRepository<MotivoTipo, String> {
    // La clave primaria es String (la descripción)
}
