package org.example.javafx_02.Factorio;

import javafx.scene.paint.Color;
import org.example.javafx_02.model.Circle;
import org.example.javafx_02.model.DrawShape;
import org.example.javafx_02.model.Square;
import org.example.javafx_02.model.Triangle;

public class FactorioShape {

    public static DrawShape createShape(String type, double x, double y, double size, Color color) {
        return switch (type) {
            case "Circle", "Круг" -> new Circle(x, y, size, color);
            case "Square", "Квадрат" -> new Square(x, y, size, color);
            case "Triangle", "Треугольник" -> new Triangle(x, y, size, color);
            default -> throw new IllegalArgumentException("Unknown shape type: " + type);
        };
    }
}
