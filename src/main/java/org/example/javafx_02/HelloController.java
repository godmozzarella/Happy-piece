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
import javafx.stage.FileChooser;
import org.example.javafx_02.model.*;
import org.example.javafx_02.model.SelectionManager;
import org.example.javafx_02.model.SelectionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


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
    private final SelectionManager selectionManager = new SelectionManager();
    private Stack<Memento> caretaker = new Stack<>();


    @FXML
    public void initialize() {
        brushTool = new BrushTool(shapes, canvas.getGraphicsContext2D());
        eraserTool = new EraserTool(shapes, canvas.getGraphicsContext2D());
        final boolean[] isShifted = {false};
        shape.getItems().addAll("Квадрат", "Круг", "Треугольник");
        shape.setValue("Квадрат");

        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.widthProperty().bind(((AnchorPane) canvas.getParent()).widthProperty());
        canvas.heightProperty().bind(((AnchorPane) canvas.getParent()).heightProperty());

        final double[] xyPosition = new double[2];

        canvas.setOnMousePressed(event -> {
            canvas.requestFocus();

            double x = event.getX();
            double y = event.getY();
            if (event.getButton() == MouseButton.PRIMARY) {
                if (isShifted[0]) {
                    SelectionUtils.handleSelection(shapes, selectionManager.getSelectedShapes(), x, y);
                    redraw(canvas.getGraphicsContext2D());
                } else if (!selectionManager.getSelectedShapes().isEmpty() &&
                            selectionManager.getSelectedShapes().stream().noneMatch(s -> x >= s.getX() - s.getSize() / 2 && y >= s.getY() - s.getSize() / 2 &&
                                    x <= s.getX() + s.getSize() / 2 && y <= s.getY() + s.getSize() / 2)){
                        selectionManager.getSelectedShapes().clear();
                        redraw(gc);

                }else if (!eraserMode && selectionManager.getSelectedShapes().isEmpty()) {
                    brushTool.startDrawing(event, shape.getValue(), sizeSlider.getValue(), color.getValue());
                    redraw(canvas.getGraphicsContext2D());
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                removeShapeAt(x, y);
                redraw(canvas.getGraphicsContext2D());
            }
        });

            canvas.setOnMouseDragged(event -> {
                double x = event.getX();
                double y = event.getY();
                double lastX, lastY;
                if(!selectionManager.getSelectedShapes().isEmpty()) {
                    if (xyPosition[0] == -1) {
                        lastX = x;
                        lastY = y;
                    } else {
                        lastX = xyPosition[0];
                        lastY = xyPosition[1];
                    }

                    xyPosition[0] = x;
                    xyPosition[1] = y;

                    selectionManager.getSelectedShapes().forEach(s -> {
                        s.setX(s.getX() + x - lastX);
                        s.setY(s.getY() + y - lastY);
                    });
                    redraw(gc);
                } else if (event.getButton() == MouseButton.PRIMARY) {
                        brushTool.draw(event);
                } else if (event.getButton() == MouseButton.SECONDARY) {
                        eraserTool.erase(event);
                }
            });

        canvas.setOnMouseReleased(event -> {
            xyPosition[0] = -1;
            xyPosition[1] = 1;
            if (event.getButton() == MouseButton.PRIMARY) {
                brushTool.stopDrawing();
            } else if (event.getButton() == MouseButton.SECONDARY) {
                eraserTool.stopErasing();
            }
        });

        color.setOnAction(event -> {
            if (selectionManager.getSelectedShapes().isEmpty()) {
                return;
            }


            selectionManager.getSelectedShapes().forEach(s -> s.setColor(color.getValue()));
            redraw(gc);
        });

        sizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (selectionManager.getSelectedShapes().isEmpty()) {
                return;
            }

            selectionManager.getSelectedShapes().forEach(s -> s.setSize(newValue.doubleValue()));
            redraw(gc);
        });


        canvas.setOnKeyPressed(keyEvent -> {
            isShifted[0] = keyEvent.isShiftDown();
            System.out.println(isShifted[0]);
        });

        canvas.setOnKeyReleased(keyEvent -> {
            isShifted[0] = keyEvent.isShiftDown();
            System.out.println(isShifted[0]);
        });

        canvas.setFocusTraversable(true);


    }


    @FXML
    private void onSaveClick() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Сохранить рисунок");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = chooser.showSaveDialog(canvas.getScene().getWindow());
        if (file != null) {
            try {
                DrawShape.saveShapesToFile(shapes, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onLoadClick() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Загрузить рисунок");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = chooser.showOpenDialog(canvas.getScene().getWindow());
        if (file != null) {
            try {
                shapes.clear();
                shapes.addAll(DrawShape.loadShapesFromFile(file));
                redraw(canvas.getGraphicsContext2D());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void redraw(GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (DrawShape s : shapes) {
            if (selectionManager.getSelectedShapes().contains(s)) {
                gc.save();
                gc.setStroke(javafx.scene.paint.Color.RED);
                gc.setLineWidth(3);
                s.draw(gc);
                gc.restore();
            } else {
                s.draw(gc);
            }
            if (selectionManager.getSelectedShapes().contains(s)) {
                gc.strokeRect(s.getX() - s.getSize() / 2 - 1, s.getY() - s.getSize() / 2 - 1, s.getSize() + 2, s.getSize() + 2);
            }
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
