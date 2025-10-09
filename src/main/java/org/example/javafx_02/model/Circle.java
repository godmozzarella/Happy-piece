package org.example.javafx_02.model;

import javafx.scene.canvas.GraphicsContext;

public class Circle extends DrawShape {
    public Circle(double x, double y, double size, javafx.scene.paint.Color color) {
        super(x, y, size, color);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(x - size / 2, y - size / 2, size, size);
    }
}