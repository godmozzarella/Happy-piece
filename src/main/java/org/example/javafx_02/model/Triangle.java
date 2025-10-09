package org.example.javafx_02.model;

import javafx.scene.canvas.GraphicsContext;

public class Triangle extends DrawShape {
    public Triangle(double x, double y, double size, javafx.scene.paint.Color color) {
        super(x, y, size, color);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        double[] xPoints = {x, x - size / 2, x + size / 2};
        double[] yPoints = {y - size / 2, y + size / 2, y + size / 2};
        gc.fillPolygon(xPoints, yPoints, 3);
    }
}