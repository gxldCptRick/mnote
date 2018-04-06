package com.gxldcptrick.mnote.models;

import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


public class ClearPackage extends DrawingPackage {


    public ClearPackage() {
        super(new SavablePoint2D(0,0), MouseEvent.MOUSE_CLICKED, new Brush());
    }
}
