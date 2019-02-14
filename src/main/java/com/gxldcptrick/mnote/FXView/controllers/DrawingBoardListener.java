package com.gxldcptrick.mnote.FXView.controllers;

import com.gxldcptrick.mnote.FXView.components.DrawingBoard;

public abstract class DrawingBoardListener {
    protected DrawingBoardListener(final DrawingBoard board){
        this.attachToBoardEvents(board);
    }

    protected abstract void attachToBoardEvents(final DrawingBoard board);
    public abstract  void detachFromBoardEvents(final DrawingBoard board);
}
