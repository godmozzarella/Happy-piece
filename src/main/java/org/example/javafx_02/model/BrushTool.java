package org.example.javafx_02.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.util.List;

public class BrushTool {
    private final List<DrawShape> shapes;
    private final GraphicsContext gc;
    private boolean drawing = false;

    private String currentShapeType;
    private double currentSize;
    private Color currentColor;

    public BrushTool(List<DrawShape> shapes, GraphicsContext gc) {
        this.shapes = shapes;
        this.gc = gc;
    }

    public void startDrawing(MouseEvent event, String shapeType, double size, Color color) {
        drawing = true;
        this.currentShapeType = shapeType;
        this.currentSize = size;
        this.currentColor = color;
        addShape(event.getX(), event.getY());
    }

    public void draw(MouseEvent event) {
        if (drawing) {
            addShape(event.getX(), event.getY());
        }
    }

    public void stopDrawing() {
        drawing = false;
    }

    private void addShape(double x, double y) {
        DrawShape shape = switch (currentShapeType) {
            case "Квадрат" -> new Square(x, y, currentSize, currentColor);
            case "Круг" -> new Circle(x, y, currentSize, currentColor);
            case "Треугольник" -> new Triangle(x, y, currentSize, currentColor);
            default -> new Square(x, y, currentSize, currentColor);
        };
        shapes.add(shape);
        shape.draw(gc);
    }
}