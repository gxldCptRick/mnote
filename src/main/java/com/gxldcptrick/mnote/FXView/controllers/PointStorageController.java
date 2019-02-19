package com.gxldcptrick.mnote.FXView.controllers;

import com.gxldcptrick.mnote.FXView.components.DrawingBoard;
import com.gxldcptrick.mnote.FXView.models.Brush;
import com.gxldcptrick.mnote.FXView.models.CanvasLines;
import com.gxldcptrick.mnote.FXView.models.MouseEventArgs;
import com.gxldcptrick.mnote.FXView.models.SavablePoint2D;
import javafx.scene.canvas.Canvas;

public class PointStorageController extends DrawingBoardListener{
   private CanvasLines lines;
   private final Brush brush;
   public PointStorageController(final DrawingBoard board, final Brush brush){
       super(board);
       this.brush = brush;
       lines = new CanvasLines();
   }

    @Override
    protected void attachToBoardEvents(DrawingBoard board) {
       board.canvasMouseDrag().subscribe(this::recordDrawingLine);
       board.canvasMouseDown().subscribe(this::recordLineStart);
       board.canvasMouseUp().subscribe(this::recordLineFinish);
    }

    @Override
    public void detachFromBoardEvents(DrawingBoard board) {
        board.canvasMouseDrag().unsubscribe(this::recordDrawingLine);
        board.canvasMouseDown().unsubscribe(this::recordLineStart);
        board.canvasMouseUp().unsubscribe(this::recordLineFinish);
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
