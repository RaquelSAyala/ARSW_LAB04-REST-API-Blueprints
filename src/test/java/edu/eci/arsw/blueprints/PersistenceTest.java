package edu.eci.arsw.blueprints;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.InMemoryBlueprintPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceTest {

    private InMemoryBlueprintPersistence persistence;

    @BeforeEach
    public void setUp() {
        persistence = new InMemoryBlueprintPersistence();
    }

    @Test
    public void shouldSaveAndRetrieveBlueprint() throws BlueprintPersistenceException, BlueprintNotFoundException {
        Blueprint bp = new Blueprint("mack", "mypainting", List.of(new Point(0, 0)));
        persistence.saveBlueprint(bp);

        Blueprint retrieved = persistence.getBlueprint("mack", "mypainting");
        assertNotNull(retrieved);
        assertEquals("mack", retrieved.getAuthor());
        assertEquals("mypainting", retrieved.getName());
    }

    @Test
    public void shouldThrowExceptionWhenBlueprintAlreadyExists() throws BlueprintPersistenceException {
        Blueprint bp = new Blueprint("mack", "mypainting", List.of(new Point(0, 0)));
        persistence.saveBlueprint(bp);

        assertThrows(BlueprintPersistenceException.class, () -> {
            persistence.saveBlueprint(bp);
        });
    }

    @Test
    public void shouldThrowExceptionWhenNotFound() {
        assertThrows(BlueprintNotFoundException.class, () -> {
            persistence.getBlueprint("nonexistent", "name");
        });
    }

    @Test
    public void shouldGetAllBlueprints() throws BlueprintPersistenceException {
        persistence.saveBlueprint(new Blueprint("a1", "b1", null));
        persistence.saveBlueprint(new Blueprint("a2", "b2", null));
        Set<Blueprint> all = persistence.getAllBlueprints();
        assertTrue(all.size() >= 2);
    }
}
