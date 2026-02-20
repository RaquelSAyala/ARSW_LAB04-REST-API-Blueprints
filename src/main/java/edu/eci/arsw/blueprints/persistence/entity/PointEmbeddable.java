package edu.eci.arsw.blueprints.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Embeddable que representa un punto (x, y) dentro de un Blueprint.
 * Se almacena en la tabla "blueprint_points".
 */
@Embeddable
public class PointEmbeddable {

    @Column(name = "x", nullable = false)
    private int x;

    @Column(name = "y", nullable = false)
    private int y;

    
    public PointEmbeddable() {
    }

    public PointEmbeddable(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
