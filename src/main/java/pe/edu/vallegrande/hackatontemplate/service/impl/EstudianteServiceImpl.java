package pe.edu.vallegrande.hackatontemplate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.vallegrande.hackatontemplate.model.Estudiante;
import pe.edu.vallegrande.hackatontemplate.repository.EstudianteRepository;
import pe.edu.vallegrande.hackatontemplate.service.EstudianteService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;

    @Override
    public Flux<Estudiante> listarTodos() {
        return estudianteRepository.findAll();
    }

    @Override
    public Flux<Estudiante> listarPorEstado(Boolean estado) {
        return estudianteRepository.findByEstado(estado);
    }

    @Override
    public Mono<Estudiante> obtenerPorId(Long id) {
        return estudianteRepository.findById(id)
                .switchIfEmpty(Mono.error(notFound(id)));
    }

    @Override
    public Mono<Estudiante> crear(Estudiante estudiante) {
        estudiante.setId(null);
        if (estudiante.getEstado() == null) {
            estudiante.setEstado(true);
        }
        return estudianteRepository.save(estudiante);
    }

    @Override
    public Mono<Estudiante> actualizar(Long id, Estudiante estudiante) {
        return estudianteRepository.findById(id)
                .switchIfEmpty(Mono.error(notFound(id)))
                .flatMap(actual -> {
                    actual.setCodigo(estudiante.getCodigo());
                    actual.setNombres(estudiante.getNombres());
                    actual.setApellidos(estudiante.getApellidos());
                    actual.setEmail(estudiante.getEmail());
                    if (estudiante.getEstado() != null) {
                        actual.setEstado(estudiante.getEstado());
                    }
                    return estudianteRepository.save(actual);
                });
    }

    @Override
    public Mono<Estudiante> cambiarEstado(Long id, Boolean estado) {
        return estudianteRepository.findById(id)
                .switchIfEmpty(Mono.error(notFound(id)))
                .flatMap(estudiante -> {
                    estudiante.setEstado(estado);
                    return estudianteRepository.save(estudiante);
                });
    }

    @Override
    public Mono<Void> eliminar(Long id) {
        return estudianteRepository.findById(id)
                .switchIfEmpty(Mono.error(notFound(id)))
                .flatMap(estudianteRepository::delete);
    }

    private ResponseStatusException notFound(Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado con id: " + id);
    }
}
