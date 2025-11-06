package org.example.javafx_02.model;

import javafx.scene.paint.Color;

public class Memento {
    private DrawShape snape;
    private double x;
    private double y;
    private double size;
    private Color color;

    public Memento(DrawShape state){
        this.snape = state;
        x= snape.getX();
        y= snape.getY();
        color= snape.getColor();
        size= snape.getSize();
    }

    public DrawShape getState() {
        snape.setX(x);
        snape.setY(y);
        snape.setColor(color);
        snape.setSize(size);
        return snape;
    }

    public void setState(DrawShape state){
        this.snape = state;
        x= snape.getX();
        y= snape.getY();
        color= snape.getColor();
        size= snape.getSize();
    }
}
