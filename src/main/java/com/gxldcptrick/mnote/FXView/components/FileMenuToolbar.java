package com.gxldcptrick.mnote.FXView.components;

import java.io.*;
import java.util.Hashtable;
import java.util.Map;

import com.gxldcptrick.mnote.FXView.events.EventHolder;
import com.gxldcptrick.mnote.commonLib.Delegate;
import com.gxldcptrick.mnote.commonLib.EventArgs;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

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

    public FileMenuToolbar(final EventHolder holder) {
        currentFileName = new Label("Current File: none");
        setupNetworkButton();
        setupFileChooser();
        setupFileButton();
        setupMenuItems(holder);
        setupNetworkItems();
        setupLayout();
    }

    private void setupLayout() {
        getChildren().addAll(file, currentFileName, network);
        this.setSpacing(10);
    }

    private void setupNetworkButton() {
        networkContext = new ContextMenu();
        network = new Button("Network");
        network.setContextMenu(networkContext);
        network.setOnAction(event -> networkContext.show(network, Side.BOTTOM, 0, 0));
    }

    private void setupFileButton() {
        fileContext = new ContextMenu();
        file = new Button("File");
        file.setContextMenu(fileContext);
        file.setOnAction(event -> fileContext.show(file, Side.BOTTOM, 0, 0));
    }

    private void setupFileChooser() {
        fileChooser = new FileChooser();
        File initialDirectory = new File("./mnote/");
        if (!initialDirectory.isDirectory()) initialDirectory.mkdir();
        fileChooser.setInitialDirectory(initialDirectory);
        fileChooser.getExtensionFilters().add(new ExtensionFilter("mnote files", ".mnote"));
        fileChooser.setInitialFileName(DEFAULT_FILE_NAME);
    }

    private void setupNetworkItems(){
        this.startSession = new MenuItem("Start Session");
        this.joinSession = new MenuItem("Join Session");
        networkContext.getItems().addAll(startSession, joinSession);
    }

    private void setupMenuItems(final EventHolder holder) {
        createTheMenuOptions();
        setupEventsForOptions(holder);
        fileContext.getItems().addAll(saveOption, saveAsOption, loadNoteOption, newNoteOption, exportAsOption);
    }

    private void setupEventsForOptions(EventHolder holder) {
        var map = new Hashtable<String, Delegate<EventArgs>>();
        map.put("Save Event" ,new Delegate<>());
        map.put("Save As Event", new Delegate<>());
        map.put("Load File Event", new Delegate<>());
        map.put("New File Event", new Delegate<>());
        map.put("Export Event", new Delegate<>());
        addHandlerToEachOption(map);
        map.forEach((eventName, event) -> holder.getEmptyEvents().addEventToRepo(event, eventName));
    }

    private void addHandlerToEachOption(Map<String, Delegate<EventArgs>> fileDelegates) {
        saveOption.setOnAction(createDelegateHandler(fileDelegates.get("Save Event")));
        saveAsOption.setOnAction(createDelegateHandler(fileDelegates.get("Save As Event")));
        loadNoteOption.setOnAction(createDelegateHandler(fileDelegates.get("Load File Event")));
        newNoteOption.setOnAction(createDelegateHandler(fileDelegates.get("New File Event")));
        exportAsOption.setOnAction(createDelegateHandler(fileDelegates.get("Export Event")));
    }

    private EventHandler<ActionEvent> createDelegateHandler(Delegate<EventArgs> event){
        return (e) -> event.invoke(this, EventArgs.EMPTY);
    }

    private void createTheMenuOptions() {
        saveOption = new MenuItem("Save");
        saveAsOption = new MenuItem("Save As");
        loadNoteOption = new MenuItem("Load Note");
        newNoteOption = new MenuItem("New Note");
        exportAsOption = new MenuItem("Export As Png");
    }

    public void updateFileName(String name) {
        if (name != null)
            this.currentFileName.setText("Working On: " + name);
    }
}