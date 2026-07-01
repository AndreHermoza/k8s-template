package pe.edu.vallegrande.hackatontemplate.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.hackatontemplate.model.Producto;
import pe.edu.vallegrande.hackatontemplate.service.ProductoService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoRest {

    private final ProductoService productoService;

    @GetMapping
    public Flux<Producto> listar() {
        return productoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Mono<Producto> obtener(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Producto> crear(@RequestBody Producto producto) {
        return productoService.crear(producto);
    }

    @PutMapping("/{id}")
    public Mono<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.actualizar(id, producto);
    }

    @PatchMapping("/estado/{id}")
    public Mono<Producto> cambiarEstado(@PathVariable Long id, @RequestParam Boolean estado) {
        return productoService.cambiarEstado(id, estado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminar(@PathVariable Long id) {
        return productoService.eliminar(id);
    }
}
