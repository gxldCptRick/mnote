package com.gxldcptrick.mnote.FXView.views;

import java.io.*;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javax.swing.*;

public class FileMenuToolbar extends HBox {

    private static final String DEFAULT_FILE_NAME = "default.co";
    private ContextMenu networkContext;
    private Button file;
    private Button network;
    private ContextMenu fileContext;
    private MenuItem saveOption;
    private MenuItem exportAsOption;
    private MenuItem saveAsOption;
    private MenuItem loadNoteOption;
    private MenuItem newNoteOption;
    private FileChooser fileChooser;

    private Label currentFileName;
    private MenuItem startSession;
    private MenuItem joinSession;

    public String getCurrentFileName() {
        return currentFileName.getText();
    }

    public FileMenuToolbar() {
        currentFileName = new Label("Current File: none");
        fileContext = new ContextMenu();
        networkContext = new ContextMenu();
        file = new Button("File");
        network = new Button("Network");
        fileChooser = new FileChooser();
        File initialDirectory = new File("./mnote/");
        if (!initialDirectory.isDirectory()) initialDirectory.mkdir();
        fileChooser.setInitialDirectory(initialDirectory);
        fileChooser.setSelectedExtensionFilter(new ExtensionFilter("mnote files", ".co"));
        fileChooser.setInitialFileName(DEFAULT_FILE_NAME);
        file.setContextMenu(fileContext);
        network.setContextMenu(networkContext);
        setupMenuItems();
        setupNetworkItems();

        file.setOnAction(event -> {
            fileContext.show(file, Side.BOTTOM, 0, 0);
        });
        network.setOnAction(event -> {
            networkContext.show(network, Side.BOTTOM, 0, 0);
        });

        getChildren().addAll(file, currentFileName, network);

        this.setSpacing(10);
    }

    public void resetFileName() {
        this.fileChooser.setInitialFileName(DEFAULT_FILE_NAME);
    }

    private void setupNetworkItems(){
        this.startSession = new MenuItem("Start Session");
        this.joinSession = new MenuItem("Join Session");
        networkContext.getItems().addAll(startSession, joinSession);
    }

    private void setupMenuItems() {
        saveOption = new MenuItem("Save");
        saveAsOption = new MenuItem("Save As");
        loadNoteOption = new MenuItem("Load Note");
        newNoteOption = new MenuItem("New Note");
        exportAsOption = new MenuItem("Export As Png");

        fileContext.getItems().addAll(saveOption, saveAsOption, loadNoteOption, newNoteOption, exportAsOption);
    }

    public void setJoinSessionAction(EventHandler<ActionEvent> event){
        if(event != null) joinSession.setOnAction(event);
    }

    public void setStartSessionAction(EventHandler<ActionEvent> event){
        if(event != null) startSession.setOnAction(event);
    }

    public void setSaveAction(EventHandler<ActionEvent> event){
        if(event != null) saveOption.setOnAction(event);
    }

    public void setSaveAsAction(EventHandler<ActionEvent> event){
        if(event != null) saveAsOption.setOnAction(event);
    }

    public void setLoadAction(EventHandler<ActionEvent> event){
        if(event != null) loadNoteOption.setOnAction(event);
    }

    public void setNewNoteAction(EventHandler<ActionEvent> event){
        if(event != null) newNoteOption.setOnAction(event);
    }

    public void setExportAsAction(EventHandler<ActionEvent> event){
        if(event != null) exportAsOption.setOnAction(event);
    }

    /// @@@@ "File reading (4 points) and writing"
    public Serializable loadFile(File loadingFile) {

        Serializable data = null;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(loadingFile))) {

            data = (Serializable) in.readObject();

        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();

        }

        return data;
    }

    public void saveFile(File newSave, Serializable data) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(newSave))) {
            System.out.println("Hi");
            out.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FileChooser getFileChooser() {
        return this.fileChooser;
    }

    public void updateFileName(String name) {
        if (name != null)
            this.currentFileName.setText("Working On: " + name);
    }
}