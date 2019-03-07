package com.gxldcptrick.mnote.components;

import com.gxldcptrick.mnote.events.RethinkEvents;
import com.gxldcptrick.mnote.models.Brush;
import com.gxldcptrick.mnote.models.DrawingPackage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class ClientCanvas extends Canvas{
    private GraphicsContext context;
    private String clientId;
    private int lastCoordinateIndex;
    private boolean startingLine;
    private String previousLineId;

    public ClientCanvas(String clientId) {
        super(1500, 500);
        this.clientId = clientId;
        context = this.getGraphicsContext2D();
        subscribeToNewPoints();
        previousLineId = "";
        startingLine = false;
        lastCoordinateIndex = 0;
    }

    private void subscribeToNewPoints() {
        RethinkEvents.getInstance().getLinesBeingDrawn().subscribe(
                this::drawLines
        );
    }

    private void drawLines(DrawingPackage drawingPackage) {
        if (!drawingPackage.getClientId().equals(clientId))
            return;

        if (!drawingPackage.getId().equals(this.previousLineId)){
            startingLine = true;
            lastCoordinateIndex = 0;
            System.out.println("Closing path");
            context.closePath();
        }

        if (startingLine){
            setUpBrush(drawingPackage.getBrush());
            this.previousLineId = drawingPackage.getId();
            startingLine = false;
            context.beginPath();
            context.moveTo(drawingPackage.getCoordinates().get(0).getX(),
                    drawingPackage.getCoordinates().get(0).getY());
            context.stroke();
            startingLine = false;
        }
        else{
            context.lineTo(drawingPackage.getCoordinates().get(lastCoordinateIndex).getX(),
                    drawingPackage.getCoordinates().get(lastCoordinateIndex).getY());
            context.stroke();
        }
        lastCoordinateIndex++;
    }

    private void setUpBrush(Brush brush) {
        context.setLineWidth(brush.getCurrentWidth());
        context.setEffect(brush.getEffect().lineEffect);
        context.setStroke(brush.getColor());
    }
}
