package com.gxldcptrick.mnote.FXView.controllers;

import com.gxldcptrick.mnote.FXView.components.DrawingBoard;
import com.gxldcptrick.mnote.FXView.events.EventHolder;
import com.gxldcptrick.mnote.FXView.models.Brush;
import com.gxldcptrick.mnote.FXView.models.CanvasLines;
import com.gxldcptrick.mnote.FXView.models.MouseEventArgs;
import com.gxldcptrick.mnote.FXView.models.SavablePoint2D;
import javafx.scene.canvas.Canvas;

public class PointStorageController extends  EventWrapper{
   private CanvasLines lines;
   private final Brush brush;
   public PointStorageController(final EventHolder holder){
       super(holder);
       this.brush = new Brush();
       lines = new CanvasLines();
   }

    @Override
    protected void attachToEvents(final EventHolder holder) {
       holder.getMouseEvents().subscribeToEvent(this::recordLineStart, "Canvas Mouse Down");
       holder.getMouseEvents().subscribeToEvent(this::recordLineFinish, "Canvas Mouse Up");
       holder.getMouseEvents().subscribeToEvent(this::recordDrawingLine, "Canvas Mouse Drag");
    }

    @Override
    public void detachFromEvents(final EventHolder holder) {
        holder.getMouseEvents().unsubscribeToEvent(this::recordLineStart, "Canvas Mouse Down");
        holder.getMouseEvents().unsubscribeToEvent(this::recordLineFinish, "Canvas Mouse Up");
        holder.getMouseEvents().unsubscribeToEvent(this::recordDrawingLine, "Canvas Mouse Drag");
    }

    private void recordLineFinish(Object sender, MouseEventArgs eventArgs) {

    }

    private void recordDrawingLine(Object sender, MouseEventArgs e){
       if(sender instanceof Canvas){
           var nativeEvent = e.event;
           lines.addNextPoint(new SavablePoint2D(nativeEvent.getX(), nativeEvent.getY()));
       }
    }

    private void recordLineStart(Object sender, MouseEventArgs e){
        if(sender instanceof Canvas){
            var nativeEvent = e.event;
            lines.startNewLine(brush);
            lines.addNextPoint(new SavablePoint2D(nativeEvent.getX(), nativeEvent.getY()));
        }
    }
}
