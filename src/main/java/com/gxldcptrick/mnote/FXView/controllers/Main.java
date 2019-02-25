package com.gxldcptrick.mnote.FXView.controllers;
import com.gxldcptrick.mnote.FXView.components.CanvasContainer;
import com.gxldcptrick.mnote.FXView.components.FileMenuToolbar;

import java.awt.Toolkit;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.gxldcptrick.mnote.FXView.events.EventHolder;
import com.gxldcptrick.mnote.FXView.models.NoteBookData;
import com.gxldcptrick.mnote.commonLib.Event;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private final static double Screen_Width;
    private final static double Screen_Height;

    static {
        Screen_Width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        Screen_Height = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    }

    private VBox mainLayout;
    private CanvasContainer drawSurface;
    private FileMenuToolbar tools;
    private File recentlyOpenedFile;
    private Stage mainStage;

    @Override
    public void start(Stage primaryStage) {
        this.mainStage = primaryStage;
        initializeStage();
        primaryStage.show();
    }

    @Override
    public void stop() {
        for (var wrapper: wrappers) {
            wrapper.detachFromEvents(EventHolder.instance);
        }
    }

    List<EventWrapper> wrappers;
    private void initializeStage() {
        initializeDrawSurface();
        initializeTools();
        initializeMainLayout();
        attachControllers();
        Scene mainScene = new Scene(mainLayout, 1500, 500);
        this.mainStage.setScene(mainScene);
        this.mainStage.setTitle("MNote");
    }

    private void attachControllers() {
        wrappers = new ArrayList<>();
        wrappers.add(new NoteController(EventHolder.instance));
        wrappers.add(new CanvasDrawingController(EventHolder.instance));
        wrappers.add(new PointStorageController(EventHolder.instance));

        wrappers.add(new FileController(EventHolder.instance));
    }

    private void initializeMainLayout() {
        mainLayout = new VBox();
        mainLayout.getChildren().addAll(tools, drawSurface.getLayoutNode());
    }

    private void initializeDrawSurface() {
        drawSurface = new CanvasContainer(Screen_Width, Screen_Height, EventHolder.instance);
        setUpCanvasBindings(drawSurface.getLayoutNode(), this.mainStage);
    }


    private void initializeTools() {
        tools = new FileMenuToolbar(EventHolder.instance);
    }


    private void setUpCanvasBindings(Pane canvas, Stage primaryStage) {
        canvas.prefWidthProperty().bind(primaryStage.widthProperty().multiply(.95));
        canvas.prefHeightProperty().bind(primaryStage.heightProperty());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
