package com.gxldcptrick.mnote.FXView.controllers;

import com.gxldcptrick.mnote.FXView.components.DrawingBoard;
import com.gxldcptrick.mnote.FXView.events.EventHolder;
import com.gxldcptrick.mnote.FXView.models.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class CanvasDrawingController extends EventWrapper{
    public final Brush brush;
    public CanvasDrawingController(final EventHolder holder){
        super(holder);
       brush = new Brush();
    }

    @Override
    protected void attachToEvents(final EventHolder holder) {
        holder.getMouseEvents().subscribeToEvent(this::startLine, "Canvas Mouse Down");
        holder.getMouseEvents().subscribeToEvent(this::endLine, "Canvas Mouse Up");
        holder.getMouseEvents().subscribeToEvent(this::drawingLines, "Canvas Mouse Drag");
    }
    @Override
    public void detachFromEvents(final EventHolder holder) {
        holder.getMouseEvents().unsubscribeToEvent(this::startLine, "Canvas Mouse Down");
        holder.getMouseEvents().unsubscribeToEvent(this::endLine, "Canvas Mouse Up");
        holder.getMouseEvents().unsubscribeToEvent(this::drawingLines, "Canvas Mouse Drag");
    }

    private void drawingLines(Object sender, MouseEventArgs e){
        System.out.println("Before if in drawingLines");
        if(sender instanceof Canvas){
            System.out.println("inside DrawingLines");
            var canvas = (Canvas) sender;
            var nativeEvent = e.event;
            var context = canvas.getGraphicsContext2D();
            System.out.println(String.format("(%s, %s)", nativeEvent.getX(), nativeEvent.getY()));
            context.lineTo(nativeEvent.getX(), nativeEvent.getY());
            context.stroke();
        }
    }
    private void startLine(Object sender, MouseEventArgs e){
        System.out.println("Before if in start line");
        if(sender instanceof Canvas){
            System.out.println("inside start line");
            var canvas = (Canvas) sender;
            var nativeEvent = e.event;
            var context = canvas.getGraphicsContext2D();
            configureContext(context);
            context.beginPath();
            context.moveTo(nativeEvent.getX(), nativeEvent.getY());
            context.stroke();
        }
    }

    private void configureContext(GraphicsContext context) {
        System.out.println(brush);
        context.setLineCap(brush.getBrushCap());
        context.setEffect(brush.getEffect().lineEffect);
        context.setLineWidth(brush.getCurrentWidth());
        context.setStroke(brush.getCurrentColor().getColor());
    }

    private void endLine(Object sender, MouseEventArgs e){
        if(sender instanceof Canvas){
            var canvas = (Canvas) sender;
            var nativeEvent = e.event;
            var context = canvas.getGraphicsContext2D();
            System.out.println(String.format("(%s, %s)", nativeEvent.getX(), nativeEvent.getY()));
            context.lineTo(nativeEvent.getX(), nativeEvent.getY());
            context.stroke();
            context.closePath();
        }
    }
}
