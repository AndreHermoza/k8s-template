package pe.edu.vallegrande.hackatontemplate.service;

import pe.edu.vallegrande.hackatontemplate.model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

    Flux<Producto> listarTodos();

    Flux<Producto> listarPorEstado(Boolean estado);

    Mono<Producto> obtenerPorId(Long id);

    Mono<Producto> crear(Producto producto);

    Mono<Producto> actualizar(Long id, Producto producto);

    Mono<Producto> cambiarEstado(Long id, Boolean estado);

    Mono<Void> eliminar(Long id);
}
