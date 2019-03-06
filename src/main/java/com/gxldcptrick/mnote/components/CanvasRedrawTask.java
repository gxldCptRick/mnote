package com.gxldcptrick.mnote.components;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.concurrent.atomic.AtomicReference;

public abstract class CanvasRedrawTask<T> extends AnimationTimer {
    private final AtomicReference<T> data = new AtomicReference<T>(null);
    private final Canvas canvas;

    public CanvasRedrawTask(Canvas canvas) {
        this.canvas = canvas;
    }

    public void requestRedraw(T dataToDraw){
        data.set(dataToDraw);
        start();
    }

    @Override
    public void handle(long l) {
        T dataToDraw = data.getAndSet(null);
        if (dataToDraw != null){
            redraw(canvas.getGraphicsContext2D(), dataToDraw);
        }
    }

    protected abstract void redraw(GraphicsContext context, T data);
}
