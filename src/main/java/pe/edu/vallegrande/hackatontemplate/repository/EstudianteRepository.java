package pe.edu.vallegrande.hackatontemplate.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.edu.vallegrande.hackatontemplate.model.Estudiante;
import reactor.core.publisher.Flux;

public interface EstudianteRepository extends ReactiveCrudRepository<Estudiante, Long> {

    Flux<Estudiante> findByEstado(Boolean estado);
}
