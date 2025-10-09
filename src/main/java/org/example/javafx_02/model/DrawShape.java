package org.example.javafx_02.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class DrawShape {
    protected double x;
    protected double y;
    protected double size;
    protected Color color;

    public DrawShape(double x, double y, double size, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    public abstract void draw(GraphicsContext gc);

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getSize() { return size; }
    public void setSize(double size) { this.size = size; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
}