package org.example.javafx_02.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.javafx_02.Factorio.FactorioShape;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    public String grouped() {
        return String.format("%s\n%.2f\n%.2f\n%.2f\n%f\n%f\n%f\n%f\n\n",
                this.getClass().getSimpleName(), x, y, size, color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
    }

    public static DrawShape ungrouped(String serialized) {
        String[] lines = serialized.lines()
                .filter(line -> !line.isBlank())
                .toArray(String[]::new);

        if (lines.length != 8) {
            throw new IllegalArgumentException("Error in serialized block: " + serialized);
        }

        String type = lines[0];
        double x = Double.parseDouble(lines[1]);
        double y = Double.parseDouble(lines[2]);
        double size = Double.parseDouble(lines[3]);
        double red = Double.parseDouble(lines[4]);
        double green = Double.parseDouble(lines[5]);
        double blue = Double.parseDouble(lines[6]);
        double opacity = Double.parseDouble(lines[7]);
        Color color = new Color(red, green, blue, opacity);
        return FactorioShape.createShape(type, x, y, size, color);
    }

    public static void saveShapesToFile(List<DrawShape> shapes, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (DrawShape shape : shapes) {
                writer.write(shape.grouped());
                writer.newLine();
            }
        }
    }

    public static List<DrawShape> loadShapesFromFile(File file) throws IOException {
        List<DrawShape> shapes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder block = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    if (!block.isEmpty()) {
                        shapes.add(DrawShape.ungrouped(block.toString()));
                        block.setLength(0);
                    }
                } else {
                    block.append(line).append("\n");
                }
            }
            if (!block.isEmpty()) {
                shapes.add(DrawShape.ungrouped(block.toString()));
            }
        }
        return shapes;
    }

    public void SetMemento(Memento state){
        state.setState(this);
    }

    public Memento CreateMemento(){

        return new Memento(this);
    }
}