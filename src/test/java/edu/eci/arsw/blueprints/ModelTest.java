package edu.eci.arsw.blueprints;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    @Test
    public void blueprintShouldBeCreatedWithPoints() {
        Point p1 = new Point(10, 10);
        Point p2 = new Point(20, 20);
        Blueprint bp = new Blueprint("john", "house", List.of(p1, p2));

        assertEquals("john", bp.getAuthor());
        assertEquals("house", bp.getName());
        assertEquals(2, bp.getPoints().size());
        assertEquals(10, bp.getPoints().get(0).x());
        assertEquals(20, bp.getPoints().get(1).x());
    }

    @Test
    public void blueprintShouldMatchSameAuthorAndName() {
        Blueprint bp1 = new Blueprint("john", "house", null);
        Blueprint bp2 = new Blueprint("john", "house", null);
        Blueprint bp3 = new Blueprint("jane", "house", null);

        
        assertEquals(bp1.getAuthor(), bp2.getAuthor());
        assertEquals(bp1.getName(), bp2.getName());
        assertNotEquals(bp1.getAuthor(), bp3.getAuthor());
    }

    @Test
    public void pointRecordShouldHaveCorrectCoordinates() {
        Point p = new Point(5, -5);
        assertEquals(5, p.x());
        assertEquals(-5, p.y());
    }
}
