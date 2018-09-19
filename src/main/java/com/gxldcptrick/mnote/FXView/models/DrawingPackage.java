package com.gxldcptrick.mnote.FXView.models;

import javafx.event.EventType;
import javafx.scene.input.MouseEvent;
import java.io.Serializable;

public class DrawingPackage implements Serializable {
    private static long serialVersionUID = 7884101105903308525l;
    private SavablePoint2D point2d;
    private String mouseEvent;
    private Brush brush;
    private boolean clearCanvas;
    private boolean isEndorStart;

    public DrawingPackage(boolean clearCanvas) {
        this.clearCanvas = clearCanvas;
    }
    public DrawingPackage(SavablePoint2D point2d, EventType<? extends MouseEvent> mouseEvent, Brush brush, boolean endingOrStarting)
    {
        this.point2d = point2d;

        if(mouseEvent  != null)
            this.mouseEvent = mouseEvent.toString();

        this.brush = brush;
        this.isEndorStart = endingOrStarting;
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
    public boolean isClearing(){
        return this.clearCanvas;
    }

    public boolean isEndorStart()
    {
     return isEndorStart;
    }


}
