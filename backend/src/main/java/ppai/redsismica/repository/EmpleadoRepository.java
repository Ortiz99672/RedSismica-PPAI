package ppai.redsismica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ppai.redsismica.entity.Empleado;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, String> {
    // JpaRepository ya nos da findById (en este caso, por mail)
}
