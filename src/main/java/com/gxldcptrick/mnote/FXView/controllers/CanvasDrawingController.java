package com.gxldcptrick.mnote.FXView.controllers;

import com.gxldcptrick.mnote.FXView.components.DrawingBoard;
import com.gxldcptrick.mnote.FXView.models.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class CanvasDrawingController {
    public final Brush brush;
    public CanvasDrawingController(final DrawingBoard board){
        brush = new Brush();
        board.canvasMouseDrag.subscribe(this::drawingLines);
        board.canvasMouseDown.subscribe(this::startLine);
        board.canvasMouseUp.subscribe(this::endLine);
    }

    public void DetachFromBoard(final DrawingBoard board){
        board.canvasMouseDrag.unsubscribe(this::drawingLines);
        board.canvasMouseDown.unsubscribe(this::startLine);
        board.canvasMouseUp.unsubscribe(this::endLine);
    }

    private void drawingLines(Object sender, MouseEventArgs e){
        System.out.println(sender);
        if(sender instanceof Canvas){
            System.out.println(sender);
            var canvas = (Canvas) sender;
            var nativeEvent = e.event;
            var context = canvas.getGraphicsContext2D();
            context.lineTo(nativeEvent.getScreenX(), nativeEvent.getScreenY());
        }
    }
    private void startLine(Object sender, MouseEventArgs e){
        System.out.println("Before if");
        if(sender instanceof Canvas){
            System.out.println(sender);
            var canvas = (Canvas) sender;
            var nativeEvent = e.event;
            var context = canvas.getGraphicsContext2D();
            context.beginPath();
            configureContext(context);
            context.moveTo(nativeEvent.getScreenX(), nativeEvent.getScreenY());
        }
    }

    private void configureContext(GraphicsContext context) {
        context.setLineCap(brush.getBrushCap());
        context.setEffect(brush.getEffect().lineEffect);
        context.setLineWidth(brush.getCurrentWidth());
        context.setFill(brush.getCurrentColor().getColor());
    }

    private void endLine(Object sender, MouseEventArgs e){
        if(sender instanceof Canvas){
            var canvas = (Canvas) sender;
            var nativeEvent = e.event;
            var context = canvas.getGraphicsContext2D();
            context.moveTo(nativeEvent.getScreenX(), nativeEvent.getScreenY());
        }
    }
}
