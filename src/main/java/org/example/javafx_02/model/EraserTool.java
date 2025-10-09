package org.example.javafx_02.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import java.util.Iterator;
import java.util.List;

public class EraserTool {
    private final List<DrawShape> shapes;
    private final GraphicsContext gc;
    private boolean erasing = false;

    private String currentShapeType;
    private double currentSize;

    public EraserTool(List<DrawShape> shapes, GraphicsContext gc) {
        this.shapes = shapes;
        this.gc = gc;
    }

    public void startErasing(MouseEvent event, String shapeType, double size) {
        erasing = true;
        this.currentShapeType = shapeType;
        this.currentSize = size;
        eraseAt(event.getX(), event.getY());
    }

    public void erase(MouseEvent event) {
        if (erasing) {
            eraseAt(event.getX(), event.getY());
        }
    }

    public void stopErasing() {
        erasing = false;
    }

    private void eraseAt(double x, double y) {
        double half = currentSize / 2;

        Iterator<DrawShape> iterator = shapes.iterator();
        while (iterator.hasNext()) {
            DrawShape shape = iterator.next();

            boolean intersects = switch (currentShapeType) {
                case "Квадрат" -> Math.abs(shape.getX() - x) <= half && Math.abs(shape.getY() - y) <= half;
                case "Круг" -> Math.hypot(shape.getX() - x, shape.getY() - y) <= half;
                case "Треугольник" -> Math.abs(shape.getX() - x) + Math.abs(shape.getY() - y) <= currentSize;
                default -> false;
            };

            if (intersects) {
                iterator.remove();
            }
        }

        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        for (DrawShape s : shapes) {
            s.draw(gc);
        }
    }
}