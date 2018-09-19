package com.gxldcptrick.mnote.FXView.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

import java.io.Serializable;
import java.util.Objects;

public class DrawingPackage implements Serializable {
    private static long serialVersionUID = 7884101105903308525l;

    private SavablePoint2D point2d;
    private String mouseEvent;
    private Brush brush;
    private boolean isClearingCanvas;
    private boolean isEndOrStart;

    public DrawingPackage() {
        this(null, null, null, false, false);
    }

    public DrawingPackage(SavablePoint2D point2d, EventType<? extends MouseEvent> mouseEvent, Brush brush, boolean isEndOrStart, boolean isClearingCanvas) {
        this.setPoint2d(point2d);
        String mouseEventName = null;
        if (mouseEvent != null) mouseEventName = mouseEvent.toString();
        this.setMouseEvent(mouseEventName);
        this.setBrush(brush);
        this.setEndOrStart(isEndOrStart);
        this.setClearingCanvas(isClearingCanvas);
    }

    public boolean equals(DrawingPackage otherPackage){
        var isEquals = false;
        if(otherPackage != null){
            isEquals = getMouseEvent().equals(otherPackage.getMouseEvent()) &&
                    getBrush().equals(otherPackage.getBrush()) &&
                    getPoint2d().equals(otherPackage.getPoint2d());
        }
        return isEquals;
    }

    @Override
    public boolean equals(Object o) {
        var isEquals = false;
        if (this == o) isEquals = true;
        else if (o == null || getClass() != o.getClass()) isEquals = false;
        else isEquals = equals((DrawingPackage) o);
        return isEquals;
    }

    @Override
    public int hashCode() {

        return Objects.hash(getPoint2d(), getMouseEvent(), getBrush(), isClearingCanvas(), isEndOrStart());
    }

    public void setEndOrStart(boolean endOrStart) {
        isEndOrStart = endOrStart;
    }

    public void setClearingCanvas(boolean clearingCanvas) {
        isClearingCanvas = clearingCanvas;
    }

    public void setBrush(Brush brush) {
        this.brush = brush;
    }

    public void setMouseEvent(String mouseEvent) {
        this.mouseEvent = mouseEvent;
    }

    public void setPoint2d(SavablePoint2D point2d) {
        this.point2d = point2d;
    }

    @JsonProperty("point")
    public SavablePoint2D getPoint2d() {
        return point2d;
    }
    public String getMouseEvent() {
        return mouseEvent;
    }
    public Brush getBrush() { return brush; }
    public boolean isClearingCanvas() {
        return isClearingCanvas;
    }
    public boolean isEndOrStart() {
        return isEndOrStart;
    }
}
