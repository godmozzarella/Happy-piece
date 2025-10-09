package org.example.javafx_02;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import org.example.javafx_02.model.*;

import java.util.ArrayList;
import java.util.List;


public class HelloController {
    @FXML
    private Canvas canvas;
    @FXML
    private ColorPicker color;
    @FXML
    private ComboBox<String> shape;
    @FXML
    private Slider sizeSlider;
    @FXML
    private Label cursor_xy;

    private final List<DrawShape> shapes = new ArrayList<>();
    private BrushTool brushTool;
    private EraserTool eraserTool;
    private boolean eraserMode = false;

    @FXML
    public void initialize() {
        shape.getItems().addAll("Квадрат", "Круг", "Треугольник");
        shape.setValue("Квадрат");

        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.widthProperty().bind(((AnchorPane) canvas.getParent()).widthProperty());
        canvas.heightProperty().bind(((AnchorPane) canvas.getParent()).heightProperty());

        canvas.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double x = event.getX();
                double y = event.getY();
                double size = sizeSlider.getValue();
                javafx.scene.paint.Color c = color.getValue();
                DrawShape drawable = switch (shape.getValue()) {
                    case "Квадрат" -> new Square(x, y, size, c);
                    case "Круг" -> new Circle(x, y, size, c);
                    case "Треугольник" -> new Triangle(x, y, size, c);
                    default -> new Square(x, y, size, c);
                };
                shapes.add(drawable);
                redraw(gc);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                removeShapeAt(event.getX(), event.getY());
                redraw(gc);
            }
        });

        brushTool = new BrushTool(shapes, gc);

        canvas.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                brushTool.startDrawing(event, shape.getValue(), sizeSlider.getValue(), color.getValue());
            }
        });

        canvas.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                brushTool.draw(event);
            }
        });

        canvas.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                brushTool.stopDrawing();
            }
        });
        canvas.setOnMouseMoved(event -> {
            double x = event.getX();
            double y = event.getY();
            cursor_xy.setText(String.format("X: %.1f, Y: %.1f", x, y));
        });

        eraserTool = new EraserTool(shapes, gc);
        canvas.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                brushTool.startDrawing(event, shape.getValue(), sizeSlider.getValue(), color.getValue());
            } else if (event.getButton() == MouseButton.SECONDARY) {
                eraserTool.startErasing(event, shape.getValue(), sizeSlider.getValue());
            }
        });

        canvas.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                brushTool.draw(event);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                eraserTool.erase(event);
            }
        });

        canvas.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                brushTool.stopDrawing();
            } else if (event.getButton() == MouseButton.SECONDARY) {
                eraserTool.stopErasing();
            }
        });
    }

    private void redraw(GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (DrawShape s : shapes) {
            s.draw(gc);
        }
    }

    private void removeShapeAt(double x, double y) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            DrawShape s = shapes.get(i);
            if (Math.abs(s.getX() - x) <= s.getSize()/2 && Math.abs(s.getY() - y) <= s.getSize()/2) {
                shapes.remove(i);
                break;
            }
        }
    }

}
