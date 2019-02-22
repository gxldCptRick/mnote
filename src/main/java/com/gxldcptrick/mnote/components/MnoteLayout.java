package com.gxldcptrick.mnote.components;

import javafx.scene.layout.VBox;

public class MnoteLayout extends VBox {
    private MenuToolbar menuToolbar;
    private CanvasToolbar canvasToolbar;
    private ScrollableCanvas canvasScrollPane;

    public MnoteLayout(){
        initializeCanvasScrollPane();
        initializeCanvasToolbar();
        initializeMenuToolbar();

        getChildren().addAll(menuToolbar, canvasToolbar, canvasScrollPane);
    }

    private void initializeMenuToolbar(){
        menuToolbar = new MenuToolbar();
    }
    private void initializeCanvasToolbar(){
        canvasToolbar = new CanvasToolbar();
    }
    private void initializeCanvasScrollPane() {
        canvasScrollPane = new ScrollableCanvas();
    }
}
