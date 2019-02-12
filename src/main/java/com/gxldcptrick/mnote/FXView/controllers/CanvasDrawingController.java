package com.gxldcptrick.mnote.FXView.controllers;

import com.gxldcptrick.mnote.FXView.components.DrawingBoard;
import com.gxldcptrick.mnote.FXView.models.*;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class CanvasDrawingController {
    private CanvasLines lines;
    private Brush currentBrush;
    public CanvasDrawingController(final DrawingBoard board){
        lines = new CanvasLines();
        currentBrush = new Brush();
        board.canvasMouseDrag.subscribe(this::drawingLines);
        board.canvasMouseDown.subscribe(this::startLine);
        board.canvasMouseUp.subscribe(this::endLine);
        board.canvasClicked.subscribe(this::deleteLine);
    }

    public Brush getBrush(){
        return this.currentBrush;
    }

    public void DetachFromBoard(final DrawingBoard board){
        board.canvasMouseDrag.unsubscribe(this::drawingLines);
        board.canvasMouseDown.unsubscribe(this::startLine);
        board.canvasMouseUp.unsubscribe(this::endLine);
        board.canvasClicked.unsubscribe(this::deleteLine);
    }

    private void drawingLines(Object sender, MouseEventArgs e){
        if(sender instanceof Canvas){
            var canvas = (Canvas) sender;
            var nativeEvent = e.event;
            var context = canvas.getGraphicsContext2D();
            context.lineTo(nativeEvent.getScreenX(), nativeEvent.getScreenY());
        }
    }
    private void startLine(Object sender, MouseEventArgs e){
        if(sender instanceof Canvas){
            var canvas = (Canvas) sender;
            var nativeEvent = e.event;
            var context = canvas.getGraphicsContext2D();
            configureContext(context);
            context.beginPath();
            context.moveTo(nativeEvent.getScreenX(), nativeEvent.getScreenY());
        }
    }

    private void configureContext(GraphicsContext context) {
        context.setLineCap(currentBrush.getBrushCap());
        context.setEffect(currentBrush.getEffect().lineEffect);
        context.setLineWidth(currentBrush.getCurrentWidth());
        context.setFill(currentBrush.getCurrentColor().getColor());
    }

    private void endLine(Object sender, MouseEventArgs e){
        if(sender instanceof Canvas){
            var canvas = (Canvas) sender;
            var nativeEvent = e.event;
            var context = canvas.getGraphicsContext2D();
            context.moveTo(nativeEvent.getScreenX(), nativeEvent.getScreenY());
        }
    }

    public void redrawSavedLines(DrawingBoard board){
        var canvas = board.getDrawSurface();
        lines.drawLines(canvas.getGraphicsContext2D());
    }

    public void redrawSavedLines(Canvas canvas){
        lines.drawLines(canvas.getGraphicsContext2D());
    }

    public void repostNotes(DrawingBoard board){
        var noteBoard = board.getNoteSurface();

    }


    private void deleteLine(Object sender, MouseEventArgs e){
        if(sender instanceof Canvas){
            var canvas = (Canvas) sender;
            redrawSavedLines(canvas);
        }
    }

    public void clearLines(){
        this.lines = new CanvasLines();
    }
}
