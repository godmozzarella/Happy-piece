package org.example.javafx_02.model;

import java.util.List;

public class SelectionUtils {
    public static void handleSelection(List<DrawShape> shapes, List<DrawShape> selected, double x, double y) {
        for (DrawShape s : shapes) {
            boolean inside = x >= s.getX() - s.getSize() / 2 && y >= s.getY() - s.getSize() / 2 &&
                    x <= s.getX() + s.getSize() / 2 && y <= s.getY() + s.getSize() / 2;
            if (inside) {
                if (selected.contains(s)) {
                    selected.remove(s);
                } else {
                    selected.add(s);
                }
            }
        }
    }
}