package com.gxldcptrick.mnote.FXView.controllers;

import com.gxldcptrick.mnote.FXView.components.DrawingBoard;
import com.gxldcptrick.mnote.FXView.models.*;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class CanvasDrawingController {
    private CanvasLines lines;
    private Notes data;
    private Brush currentBrush;
    public CanvasDrawingController(final DrawingBoard board){
        lines = new CanvasLines();
        data = new Notes();
        currentBrush = new Brush();
        board.canvasMouseDragEvent.subscribe(this::drawingLines);
        board.canvasMouseDownEvent.subscribe(this::startLine);
        board.canvasMouseUpEvent.subscribe(this::endLine);
        board.canvasClickedEvent.subscribe(this::deleteLine);
        board.noteDoubleClickedEvent.subscribe(this::addNoteToScreen);
    }

    public Brush getBrush(){
        return this.currentBrush;
    }

    public void DetachFromBoard(final DrawingBoard board){
        board.canvasMouseDragEvent.unsubscribe(this::drawingLines);
        board.canvasMouseDownEvent.unsubscribe(this::startLine);
        board.canvasMouseUpEvent.unsubscribe(this::endLine);
        board.canvasClickedEvent.unsubscribe(this::deleteLine);
        board.noteDoubleClickedEvent.unsubscribe(this::addNoteToScreen);
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

    public void repostNotes(DrawingBoard board){
        var noteBoard = board.getNoteSurface();

    }


    private void deleteLine(Object sender, MouseEventArgs e){
        if(sender instanceof Canvas){
            var canvas = (Canvas) sender;
            redrawSavedLines(canvas);
        }
    }

    private void addNoteToScreen(Object sender, MouseEventArgs e){
        if(sender instanceof Group){
            var group = (Group) sender;
        }
    }

    public void clearNotes(){
        this.data.clear();
    }

    public void clearLines(){
        this.lines = new CanvasLines();
    }
}
