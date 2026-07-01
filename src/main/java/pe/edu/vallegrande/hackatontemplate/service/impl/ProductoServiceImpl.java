package pe.edu.vallegrande.hackatontemplate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.vallegrande.hackatontemplate.model.Producto;
import pe.edu.vallegrande.hackatontemplate.repository.ProductoRepository;
import pe.edu.vallegrande.hackatontemplate.service.ProductoService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public Flux<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Flux<Producto> listarPorEstado(Boolean estado) {
        return productoRepository.findByEstado(estado);
    }

    @Override
    public Mono<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .switchIfEmpty(Mono.error(notFound(id)));
    }

    @Override
    public Mono<Producto> crear(Producto producto) {
        producto.setId(null);
        if (producto.getEstado() == null) {
            producto.setEstado(true);
        }
        return productoRepository.save(producto);
    }

    @Override
    public Mono<Producto> actualizar(Long id, Producto producto) {
        return productoRepository.findById(id)
                .switchIfEmpty(Mono.error(notFound(id)))
                .flatMap(actual -> {
                    actual.setCodigo(producto.getCodigo());
                    actual.setNombre(producto.getNombre());
                    actual.setDescripcion(producto.getDescripcion());
                    actual.setPrecio(producto.getPrecio());
                    if (producto.getEstado() != null) {
                        actual.setEstado(producto.getEstado());
                    }
                    return productoRepository.save(actual);
                });
    }

    @Override
    public Mono<Producto> cambiarEstado(Long id, Boolean estado) {
        return productoRepository.findById(id)
                .switchIfEmpty(Mono.error(notFound(id)))
                .flatMap(producto -> {
                    producto.setEstado(estado);
                    return productoRepository.save(producto);
                });
    }

    @Override
    public Mono<Void> eliminar(Long id) {
        return productoRepository.findById(id)
                .switchIfEmpty(Mono.error(notFound(id)))
                .flatMap(productoRepository::delete);
    }

    private ResponseStatusException notFound(Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con id: " + id);
    }
}
