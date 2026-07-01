package pe.edu.vallegrande.hackatontemplate.service;

import pe.edu.vallegrande.hackatontemplate.model.Estudiante;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EstudianteService {

    Flux<Estudiante> listarTodos();

    Flux<Estudiante> listarPorEstado(Boolean estado);

    Mono<Estudiante> obtenerPorId(Long id);

    Mono<Estudiante> crear(Estudiante estudiante);

    Mono<Estudiante> actualizar(Long id, Estudiante estudiante);

    Mono<Estudiante> cambiarEstado(Long id, Boolean estado);

    Mono<Void> eliminar(Long id);
}
