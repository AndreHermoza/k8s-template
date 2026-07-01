package pe.edu.vallegrande.hackatontemplate.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.hackatontemplate.model.Estudiante;
import pe.edu.vallegrande.hackatontemplate.service.EstudianteService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
public class EstudianteRest {

    private final EstudianteService estudianteService;

    @GetMapping
    public Flux<Estudiante> listar() {
        return estudianteService.listarTodos();
    }

    @GetMapping("/{id}")
    public Mono<Estudiante> obtener(@PathVariable Long id) {
        return estudianteService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Estudiante> crear(@RequestBody Estudiante estudiante) {
        return estudianteService.crear(estudiante);
    }

    @PutMapping("/{id}")
    public Mono<Estudiante> actualizar(@PathVariable Long id, @RequestBody Estudiante estudiante) {
        return estudianteService.actualizar(id, estudiante);
    }

    @PatchMapping("/estado/{id}")
    public Mono<Estudiante> cambiarEstado(@PathVariable Long id, @RequestParam Boolean estado) {
        return estudianteService.cambiarEstado(id, estado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminar(@PathVariable Long id) {
        return estudianteService.eliminar(id);
    }
}
