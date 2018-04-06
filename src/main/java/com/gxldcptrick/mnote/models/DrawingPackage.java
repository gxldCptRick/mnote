package com.gxldcptrick.mnote.models;

import javafx.event.EventType;
import javafx.scene.input.MouseEvent;
import java.io.Serializable;

public class DrawingPackage implements Serializable {
    private SavablePoint2D point2d;
    private String mouseEvent;
    private Brush brush;

    public DrawingPackage(SavablePoint2D point2d, EventType<? extends MouseEvent> mouseEvent, Brush brush) {
        this.point2d = point2d;
        this.mouseEvent = mouseEvent.toString();
        this.brush = brush;
    }

    public SavablePoint2D getPoint2d() {
        return point2d;
    }
    public String getMouseEvent() {
        return mouseEvent;
    }
    public Brush getBrush() {
        return brush;
    }

    public boolean isEmpty(){

        return this.point2d == null && this.mouseEvent == null && this.brush == null;

    }

}
