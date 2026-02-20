package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.entity.BlueprintEntity;
import edu.eci.arsw.blueprints.persistence.entity.PointEmbeddable;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Persistencia con PostgreSQL vía Spring Data JPA.
 * Activo con el perfil Spring "postgres".
 */
@Repository
@Profile("postgres")
public class PostgresBlueprintPersistence implements BlueprintPersistence {

    private final BlueprintJpaRepository jpaRepo;

    public PostgresBlueprintPersistence(BlueprintJpaRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    /** Carga datos de ejemplo al iniciar con perfil postgres */
    @PostConstruct
    public void seedData() {
        if (jpaRepo.count() == 0) {
            save(new Blueprint("john", "house",
                    List.of(new Point(0, 0), new Point(10, 0), new Point(10, 10), new Point(0, 10))));
            save(new Blueprint("john", "garage",
                    List.of(new Point(5, 5), new Point(15, 5), new Point(15, 15))));
            save(new Blueprint("jane", "garden",
                    List.of(new Point(2, 2), new Point(3, 4), new Point(6, 7))));
        }
    }

    // ── Conversiones dominio ↔ entidad ──────────────────────────────────────

    private BlueprintEntity toEntity(Blueprint bp) {
        List<PointEmbeddable> pts = bp.getPoints().stream()
                .map(p -> new PointEmbeddable(p.x(), p.y()))
                .collect(Collectors.toList());
        return new BlueprintEntity(bp.getAuthor(), bp.getName(), pts);
    }

    private Blueprint toDomain(BlueprintEntity e) {
        List<Point> pts = e.getPoints().stream()
                .map(p -> new Point(p.getX(), p.getY()))
                .collect(Collectors.toList());
        return new Blueprint(e.getAuthor(), e.getName(), pts);
    }

    private void save(Blueprint bp) {
        jpaRepo.save(toEntity(bp));
    }

    // ── Implementación de BlueprintPersistence ───────────────────────────────

    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (jpaRepo.findByAuthorAndName(bp.getAuthor(), bp.getName()).isPresent()) {
            throw new BlueprintPersistenceException(
                    "Blueprint ya existe: %s/%s".formatted(bp.getAuthor(), bp.getName()));
        }
        jpaRepo.save(toEntity(bp));
    }

    @Override
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        return jpaRepo.findByAuthorAndName(author, name)
                .map(this::toDomain)
                .orElseThrow(() -> new BlueprintNotFoundException(
                        "Blueprint no encontrado: %s/%s".formatted(author, name)));
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        List<BlueprintEntity> entities = jpaRepo.findByAuthor(author);
        if (entities.isEmpty()) {
            throw new BlueprintNotFoundException("No hay blueprints para el autor: " + author);
        }
        return entities.stream().map(this::toDomain).collect(Collectors.toSet());
    }

    @Override
    public Set<Blueprint> getAllBlueprints() {
        return jpaRepo.findAll().stream().map(this::toDomain).collect(Collectors.toSet());
    }

    @Override
    public void addPoint(String author, String name, int x, int y) throws BlueprintNotFoundException {
        BlueprintEntity entity = jpaRepo.findByAuthorAndName(author, name)
                .orElseThrow(() -> new BlueprintNotFoundException(
                        "Blueprint no encontrado: %s/%s".formatted(author, name)));
        entity.addPoint(new PointEmbeddable(x, y));
        jpaRepo.save(entity);
    }
}
