package com.gxldcptrick.mnote.FXView.controllers;

import com.gxldcptrick.mnote.FXView.controllers.CanvasContainer;
import com.gxldcptrick.mnote.FXView.views.FileMenuToolbar;

import java.awt.Toolkit;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.event.ActionEvent;
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
        this.drawSurface.killNetworkConnection();
    }

    private void initializeStage() {
        initializeDrawSurface();
        initializeTools();
        initializeMainLayout();
        Scene mainScene = new Scene(mainLayout, 1500, 500);
        this.mainStage.setScene(mainScene);
        this.mainStage.setTitle("MNote");
    }

    private void initializeMainLayout() {
        mainLayout = new VBox();
        mainLayout.getChildren().addAll(tools, drawSurface.getLayout());
    }

    private void initializeDrawSurface() {
        drawSurface = new CanvasContainer(Screen_Width, Screen_Height);
        setUpCanvasBindings(drawSurface.getLayout(), this.mainStage);
    }

    private void saveAction(ActionEvent event) {
        if (this.recentlyOpenedFile == null) {
            this.tools.resetFileName();
            File newSave = tools.getFileChooser().showSaveDialog(this.mainStage);
            saveFile(newSave);
        } else {
            saveFile(this.recentlyOpenedFile);
        }
    }

    private void saveAsAction(ActionEvent event) {
        if (this.recentlyOpenedFile != null) {
            tools.getFileChooser().setInitialFileName(this.recentlyOpenedFile.getName());
        }
        File overwriteSave = tools.getFileChooser().showSaveDialog(this.mainStage);
        saveFile(overwriteSave);
    }

    private void loadAction(ActionEvent event) {
        File oldSave = tools.getFileChooser().showOpenDialog(this.mainStage);
        if (oldSave != null && oldSave.getName().endsWith(".co")) {
            mainLayout.getChildren().remove(this.drawSurface.getLayout());
            this.drawSurface = (CanvasContainer) tools.loadFile(oldSave);
            Pane canvas = drawSurface.getLayout();
            mainLayout.getChildren().add(canvas);
            this.setUpCanvasBindings(canvas, this.mainStage);
            updateToolBarText(oldSave);
        }
    }

    private void updateToolBarText(File oldSave) {
        tools.updateFileName(oldSave.getName());
        tools.getFileChooser().setInitialFileName(oldSave.getName());
        this.recentlyOpenedFile = oldSave;
    }

    private void newNoteAction(ActionEvent event) {
        promptToSave();
        initializeDrawSurface();
        initializeMainLayout();
        Scene mainScene = new Scene(mainLayout, 1500, 500);
        this.mainStage.setScene(mainScene);
    }

    private void exportAsAction(ActionEvent event) {
        File imageFile = tools.getFileChooser().showSaveDialog(this.mainStage);
        if (!imageFile.getName().endsWith(".png")) {
            imageFile = new File(imageFile.getAbsolutePath() + ".png");
        }
        RenderedImage canvas = this.drawSurface.getRenderedImage();
        writeImageToFile(canvas, "png", imageFile);
    }

    private void initializeTools() {
        tools = new FileMenuToolbar();
        tools.setSaveAction(this::saveAction);
        tools.setSaveAsAction(this::saveAsAction);
        tools.setLoadAction(this::loadAction);
        tools.setNewNoteAction(this::newNoteAction);
        tools.setExportAsAction(this::exportAsAction);
    }

    private void setUpCanvasBindings(Pane canvas, Stage primaryStage) {
        canvas.prefWidthProperty().bind(primaryStage.widthProperty().multiply(.95));
        canvas.prefHeightProperty().bind(primaryStage.heightProperty());
    }

    private void writeImageToFile(RenderedImage image, String formatName, File fileName) {
        try {
            ImageIO.write(image, formatName, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void promptToSave() {
        Dialog<?> savePop = new Alert(Alert.AlertType.CONFIRMATION);
        savePop.setContentText("Would You like to save your changes?");

        savePop.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                File save = tools.getFileChooser().showSaveDialog(this.mainStage);
                saveFile(save);
            }
        });
    }

    private void saveFile(File save) {
        if (save != null) {
            if (!save.getAbsolutePath().endsWith(".co"))
                save = new File(save.getAbsolutePath() + ".co");

            tools.saveFile(save, drawSurface);
            if (save != this.recentlyOpenedFile) {
                this.recentlyOpenedFile = save;
                this.tools.updateFileName(recentlyOpenedFile.getName());
            }
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
