package com.gxldcptrick.mnote.FXView.components;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.gxldcptrick.mnote.FXView.events.EventHolder;
import com.gxldcptrick.mnote.FXView.models.Brush;
import com.gxldcptrick.mnote.FXView.enums.SpecialEffect;

import com.gxldcptrick.mnote.FXView.models.BrushChangedEventArgs;
import com.gxldcptrick.mnote.FXView.models.SavableColor;
import com.gxldcptrick.mnote.commonLib.Delegate;
import com.gxldcptrick.mnote.commonLib.EventArgs;
import javafx.geometry.Side;

import javafx.scene.control.*;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;

public class CanvasToolbar {
    private static final List<Double> increaseSizeValues;

    static {
        increaseSizeValues = Arrays.asList(.1, .2, .3, .4, .5, .6, .7, .8, .9, 1.0, 10.0, 100.0);
    }

    private Brush userBrush;
    private HBox layout;
    private Label currentSize;
    private ContextMenu clearContextMenu;
    private Button eraseButton;
    private ColorPicker colorPicker;
    private ComboBox<Double> sizePicker;
    private CheckBox deleting;
    private ComboBox<SpecialEffect> specialEffects;
    private Map<String, Delegate<EventArgs>> emptyEvents;
    private Delegate<BrushChangedEventArgs> brushChanged;


    public CanvasToolbar(final EventHolder holder) {
        this.userBrush = new Brush();
        setupDelegateEvent(holder);
        setupLayout();
        setupEraseButton();
        setupIsDeletingBox();
        setupSpecialEffects();
        setupCurrentSizeLabel();
        setupColorPicker();
        setupSizeComboBox();
        createToolbarItemsAndSetupItems();

    }

    private void setupDelegateEvent(final EventHolder holder) {
        brushChanged = new Delegate<>();
        emptyEvents = new Hashtable<>();
        emptyEvents.put("Canvas Cleared", new Delegate<>());
        emptyEvents.put("Notes Cleared", new Delegate<>());
        holder.getBrushEvents().addEventToRepo(brushChanged, "Brush Changed");
        emptyEvents.forEach((eventName, event) -> holder.getEmptyEvents().addEventToRepo(event, eventName));
    }

    private void createToolbarItemsAndSetupItems() {
        // creating menu items
        var clearDrawings = new MenuItem("Clear Drawings");
        var clearNotes = new MenuItem("Clear Notes");
        var clearScreen = new MenuItem("Clear Everything");
        //gather events
        var clearDrawingEvent = emptyEvents.get("Canvas Cleared");
        var clearNotesEvent = emptyEvents.get("Notes Cleared");
        //setting up the action event handlers
        clearDrawings.setOnAction((e) -> clearDrawingEvent.invoke(this, EventArgs.EMPTY));
        clearNotes.setOnAction((e) -> clearNotesEvent.invoke(this, EventArgs.EMPTY));
        clearScreen.setOnAction((e) -> {
            clearDrawingEvent.invoke(this, EventArgs.EMPTY);
            clearNotesEvent.invoke(this, EventArgs.EMPTY);
        });
        //add them to the context menu
        clearContextMenu.getItems().addAll(clearDrawings, clearNotes, clearScreen);
    }

    public Pane getLayout() {
        return layout;
    }


    private void setupEraseButton() {
        this.clearContextMenu = new ContextMenu();
        this.eraseButton = new Button("Clear");
        this.eraseButton.setContextMenu(clearContextMenu);
        this.eraseButton.setOnAction(event -> this.clearContextMenu.show(eraseButton, Side.RIGHT, 0, 0));
    }

    private void setupIsDeletingBox() {
        this.deleting = new CheckBox("Delete Line");
        this.setupEraseButton();
        determineDeleting();
        this.deleting.setOnAction(event -> {
            determineDeleting();
            this.brushChanged.invoke(this, new BrushChangedEventArgs(this.userBrush));
        });
    }

    private void setupCurrentSizeLabel() {
        currentSize = new Label("Current Line Width : " + this.userBrush.getCurrentWidth());
    }

    private void setupLayout() {
        layout = new HBox();
        layout.setSpacing(10);
        layout.getChildren().addAll(currentSize, colorPicker, sizePicker, this.eraseButton, this.specialEffects, this.deleting);
    }

    private void setupSpecialEffects() {
        this.specialEffects = new ComboBox<>();
        this.specialEffects.getItems().addAll(SpecialEffect.values());
        this.specialEffects.setValue(SpecialEffect.None);
        this.userBrush.setEffect(this.specialEffects.getValue());
        this.specialEffects.setOnAction(event -> {
            this.userBrush.setEffect(this.specialEffects.getValue());
            this.brushChanged.invoke(this, new BrushChangedEventArgs(this.userBrush));
        });
    }

    private void determineDeleting() {
        userBrush.setDeleting(deleting.isSelected());
    }

    private void setupColorPicker() {
        this.colorPicker = new ColorPicker();
        colorPicker.setValue(Color.BLACK);
        colorPicker.setOnAction((event) -> {
            var color = this.colorPicker.getValue();
            this.userBrush.setCurrentColor(new SavableColor(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity()));
            this.brushChanged.invoke(this, new BrushChangedEventArgs(this.userBrush));
        });
    }

    private void setupSizeComboBox() {
        this.sizePicker = new ComboBox<>();
        sizePicker.getItems().addAll(CanvasToolbar.increaseSizeValues);
        sizePicker.setValue(this.userBrush.getCurrentWidth());
        sizePicker.setOnAction((event) -> {
            this.userBrush.setCurrentWidth(this.sizePicker.getValue());
            updateSize();
            this.brushChanged.invoke(this, new BrushChangedEventArgs(this.userBrush));
        });
    }

    private void updateSize() {
        currentSize.setText("Current Line Width: " + this.userBrush.getCurrentWidth());
    }

    public boolean equals(CanvasToolbar other) {
        return other.userBrush.equals(this.userBrush);
    }

    @Override
    public boolean equals(Object other) {
        boolean equal = false;
        if (other instanceof CanvasToolbar) equal = equals((CanvasToolbar) other);
        return equal;
    }

    @Override
    public int hashCode() {
        return this.userBrush.hashCode();
    }
}