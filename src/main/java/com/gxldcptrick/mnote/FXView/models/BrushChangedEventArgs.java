package com.gxldcptrick.mnote.FXView.models;

import com.gxldcptrick.mnote.commonLib.EventArgs;

public class BrushChangedEventArgs extends EventArgs {
    private Brush brush;

    public BrushChangedEventArgs(Brush brush){
        this.brush = new Brush(brush);
    }

    public Brush getBrush() {
        return brush;
    }
}
