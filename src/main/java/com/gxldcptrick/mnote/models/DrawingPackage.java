package com.gxldcptrick.mnote.models;

import java.io.Serializable;

public class DrawingPackage implements Serializable {
    private SavablePoint2D point2d;
    private String mouseEvent;
    private Brush brush;

    public DrawingPackage(SavablePoint2D point2d, String mouseEvent, Brush brush) {
        this.point2d = point2d;
        this.mouseEvent = mouseEvent;
        this.brush = brush;
    }

    public SavablePoint2D getPoint2d() {
        return point2d;
    }

    public void setPoint2d(SavablePoint2D point2d) {
        this.point2d = point2d;
    }

    public String getMouseEvent() {
        return mouseEvent;
    }

    public void setMouseEvent(String mouseEvent) {
        this.mouseEvent = mouseEvent;
    }

    public Brush getBrush() {
        return brush;
    }

    public void setBrush(Brush brush) {
        this.brush = brush;
    }
}
