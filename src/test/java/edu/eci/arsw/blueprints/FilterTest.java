package edu.eci.arsw.blueprints;

import edu.eci.arsw.blueprints.filters.IdentityFilter;
import edu.eci.arsw.blueprints.filters.RedundancyFilter;
import edu.eci.arsw.blueprints.filters.UndersamplingFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pruebas unitarias para la lógica de los filtros de Blueprints.
 */
public class FilterTest {

    @Test
    public void redundancyFilterShouldRemoveConsecutiveDuplicatePoints() {
        RedundancyFilter filter = new RedundancyFilter();

       
        List<Point> points = new ArrayList<>(List.of(
                new Point(1, 1),
                new Point(1, 1),
                new Point(2, 2),
                new Point(2, 2),
                new Point(2, 2),
                new Point(3, 3)));

        Blueprint bp = new Blueprint("author", "testBP", points);
        Blueprint filtered = filter.apply(bp);

       
        assertEquals(3, filtered.getPoints().size(),
                "Deberían quedar exactamente 3 puntos tras eliminar duplicados consecutivos");
        assertEquals(1, filtered.getPoints().get(0).x());
        assertEquals(2, filtered.getPoints().get(1).x());
        assertEquals(3, filtered.getPoints().get(2).x());
    }

    @Test
    public void redundancyFilterShouldKeepNonConsecutiveDuplicatePoints() {
        RedundancyFilter filter = new RedundancyFilter();

        
        List<Point> points = new ArrayList<>(List.of(
                new Point(1, 1),
                new Point(2, 2),
                new Point(1, 1)));

        Blueprint bp = new Blueprint("author", "testBP", points);
        Blueprint filtered = filter.apply(bp);

        assertEquals(3, filtered.getPoints().size(), "No debería eliminar puntos que no son consecutivos");
    }

    @Test
    public void undersamplingFilterShouldRemoveEveryOtherPoint() {
        UndersamplingFilter filter = new UndersamplingFilter();

        // 4 puntos: (0,0), (1,1), (2,2), (3,3)
        List<Point> points = new ArrayList<>(List.of(
                new Point(0, 0),
                new Point(1, 1),
                new Point(2, 2),
                new Point(3, 3)));

        Blueprint bp = new Blueprint("author", "testBP", points);
        Blueprint filtered = filter.apply(bp);

        // Deberían quedar 2 puntos (los índices pares 0 y 2)
        assertEquals(2, filtered.getPoints().size(),
                "El filtro undersampling debería dejar la mitad de los puntos (para 4 puntos)");
        assertEquals(0, filtered.getPoints().get(0).x());
        assertEquals(2, filtered.getPoints().get(1).x());
    }

    @Test
    public void identityFilterShouldNotModifyPoints() {
        IdentityFilter filter = new IdentityFilter();

        List<Point> points = new ArrayList<>(List.of(
                new Point(10, 10),
                new Point(20, 20)));

        Blueprint bp = new Blueprint("author", "testBP", points);
        Blueprint filtered = filter.apply(bp);

        assertEquals(2, filtered.getPoints().size(), "IdentityFilter no debe cambiar el número de puntos");
        assertEquals(10, filtered.getPoints().get(0).x());
        assertEquals(20, filtered.getPoints().get(1).x());
    }
}
