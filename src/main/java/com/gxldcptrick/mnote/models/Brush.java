package com.gxldcptrick.mnote.models;

import javafx.scene.canvas.GraphicsContext;

public class Brush {
    private BrushSettings settings;
    private GraphicsContext context;

    public Brush(GraphicsContext context) {

        this.context = context;
        this.settings = new BrushSettings();

    }

    public GraphicsContext getContext() {
        return context;
    }


    public BrushSettings getSettings() {
        return settings;
    }

}
