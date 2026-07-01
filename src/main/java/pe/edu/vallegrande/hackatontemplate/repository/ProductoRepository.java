package pe.edu.vallegrande.hackatontemplate.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.edu.vallegrande.hackatontemplate.model.Producto;
import reactor.core.publisher.Flux;

public interface ProductoRepository extends ReactiveCrudRepository<Producto, Long> {

    Flux<Producto> findByEstado(Boolean estado);
}
