package org.example.javafx_02.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class SelectionManager {
    private final List<DrawShape> selectedShapes = new ArrayList<>();

    public List<DrawShape> getSelectedShapes() {
        return selectedShapes;
    }
}