package edu.eci.arsw.blueprints;

import edu.eci.arsw.blueprints.filters.BlueprintsFilter;
import edu.eci.arsw.blueprints.filters.IdentityFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.InMemoryBlueprintPersistence;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

    private BlueprintsServices services;
    private InMemoryBlueprintPersistence persistence;
    private BlueprintsFilter filter;

    @BeforeEach
    public void setUp() {
        persistence = new InMemoryBlueprintPersistence();
        filter = new IdentityFilter(); // Usamos identidad para probar la orquestación básica
        services = new BlueprintsServices(persistence, filter);
    }

    @Test
    public void serviceShouldAddNewBlueprint() throws BlueprintPersistenceException, BlueprintNotFoundException {
        Blueprint bp = new Blueprint("testing", "serviceBP", List.of(new Point(1, 1)));
        services.addNewBlueprint(bp);

        Blueprint retrieved = services.getBlueprint("testing", "serviceBP");
        assertEquals("testing", retrieved.getAuthor());
    }

    @Test
    public void serviceShouldRetrieveByAuthor() throws BlueprintPersistenceException, BlueprintNotFoundException {
        services.addNewBlueprint(new Blueprint("authorX", "bp1", null));
        services.addNewBlueprint(new Blueprint("authorX", "bp2", null));

        assertEquals(2, services.getBlueprintsByAuthor("authorX").size());
    }

    @Test
    public void serviceShouldApplyFilter() throws BlueprintPersistenceException, BlueprintNotFoundException {
       
        Point p = new Point(10, 10);
        services.addNewBlueprint(new Blueprint("testUser", "testBP", List.of(p, p)));

        Blueprint retrieved = services.getBlueprint("testUser", "testBP");
        assertEquals(2, retrieved.getPoints().size());
    }
}
