package com.gxldcptrick.mnote.components;

import com.gxldcptrick.mnote.events.RethinkEvents;
import com.gxldcptrick.mnote.models.Brush;
import com.gxldcptrick.mnote.models.DrawingPackage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public class ClientCanvas extends Canvas{
    private GraphicsContext context;
    private String clientId;
    private DrawingPackage previouesDrawingPackage;
    private int lastCoordinateIndex;
    private int drawingPackageCoordinateLength;

    public ClientCanvas(String clientId) {
        super(1500, 500);
        this.clientId = clientId;
        context = this.getGraphicsContext2D();
        subscribeToNewPoints();
        previouesDrawingPackage = new DrawingPackage();
        lastCoordinateIndex = 0;
    }

    private void subscribeToNewPoints() {
        RethinkEvents.getInstance().getLinesBeingDrawn().buffer(50).subscribe(
                this::drawLines
        );
    }

    private void drawLines(List<DrawingPackage> drawingPackages) {
        context.beginPath();;
        context.moveTo(drawingPackages.get(49).getCoordinates().get(0).getX(),
                drawingPackages.get(49).getCoordinates().get(0).getY());
        for(var bla : drawingPackages.get(49).getCoordinates()){
            context.lineTo(bla.getX(), bla.getY());
            context.stroke();
        }
    }

    private void drawLines(DrawingPackage drawingPackage) {
        if (!drawingPackage.getClientId().equals(clientId))
            return;

//        if (!previouesDrawingPackage.getId().equals(drawingPackage.getId())){
//            setUpBrush(drawingPackage.getBrush());
//        }

        drawingPackageCoordinateLength = drawingPackage.getCoordinates().size();
        for(int i = lastCoordinateIndex; i < drawingPackageCoordinateLength; i++){
            if (i == 0){
                context.beginPath();
                context.moveTo(drawingPackage.getCoordinates().get(i).getX(),
                        drawingPackage.getCoordinates().get(i).getY());
            }
            else {
                context.lineTo(drawingPackage.getCoordinates().get(i).getX(),
                        drawingPackage.getCoordinates().get(i).getY());
            }
            context.stroke();
        }
        lastCoordinateIndex = drawingPackageCoordinateLength;

        System.out.println("-----------------------------------------------------");
        System.out.println("Last coordinate Index:" + lastCoordinateIndex);
        System.out.println("-----------------------------------------------------");
        context.closePath();
    }

    private void setUpBrush(Brush brush) {
        context.setLineWidth(brush.getCurrentWidth());
        context.setEffect(brush.getEffect().lineEffect);
        context.setStroke(brush.getColor());
    }
}
