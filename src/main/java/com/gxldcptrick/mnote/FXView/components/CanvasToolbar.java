package com.gxldcptrick.mnote.FXView.components;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.Arrays;
import java.util.List;

import com.gxldcptrick.mnote.FXView.models.Brush;
import com.gxldcptrick.mnote.FXView.enums.SpecialEffect;

import com.gxldcptrick.mnote.FXView.models.SavableColor;
import javafx.geometry.Side;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;

public class CanvasToolbar implements Serializable {
    private static final long serialVersionUID = 556677L;
    private static final List<Double> increaseSizeValues;

    static {
        increaseSizeValues = Arrays.asList(.1, .2, .3, .4, .5, .6, .7, .8, .9, 1.0, 10.0, 100.0);
    }

    private Brush userBrush;
    private transient HBox layout;
    private transient Label currentSize;
    private transient ContextMenu contextMenu;
    private transient Button eraseButton;
    private transient ColorPicker colorPicker;
    private transient ComboBox<Double> sizePicker;
    private transient CheckBox deleting;
    private transient ComboBox<SpecialEffect> specialEffects;

    public CanvasToolbar(Brush userBrush) {
        initializeToolbar(userBrush);
    }

    public Pane getLayout() {
        return layout;
    }

    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }


    private void initializeEraseButton() {
        this.contextMenu = new ContextMenu();
        this.eraseButton = new Button("Clear");
        this.eraseButton.setContextMenu(contextMenu);
        this.eraseButton.setOnAction(event -> this.contextMenu.show(eraseButton, Side.RIGHT, 0, 0));
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        initializeToolbar(this.userBrush);
    }

    private void initializeToolbar(Brush userBrush) {
        this.userBrush = userBrush;
        initializeDeletingButton();
        initializeSpecialEffects();
        initializeCurrentSizeLabel();
        initializeColorPicker();
        initializeIncreaseSizeComboBox();
        initializeLayout();
    }

    private void initializeDeletingButton() {
        this.deleting = new CheckBox("Delete Line");
        this.initializeEraseButton();
        determineDeleting();
        this.deleting.setOnAction(event -> determineDeleting());
    }

    private void initializeCurrentSizeLabel() {
        currentSize = new Label("Current Line Width : " + this.userBrush.getCurrentWidth());
    }

    private void initializeLayout() {
        layout = new HBox();
        layout.setSpacing(10);
        layout.getChildren().addAll(currentSize, colorPicker, sizePicker, this.eraseButton, this.specialEffects, this.deleting);
    }

    private void initializeSpecialEffects() {
        this.specialEffects = new ComboBox<>();
        this.specialEffects.getItems().addAll(SpecialEffect.values());
        this.specialEffects.setValue(SpecialEffect.None);
        this.userBrush.setEffect(this.specialEffects.getValue());
        this.specialEffects.setOnAction(event -> this.userBrush.setEffect(this.specialEffects.getValue()));
    }

    private void determineDeleting() {
        userBrush.setDeleting(deleting.isSelected());
    }

    private void initializeColorPicker() {
        this.colorPicker = new ColorPicker();
        colorPicker.setValue(Color.BLACK);
        colorPicker.setOnAction((event) -> {
            var color = this.colorPicker.getValue();
            this.userBrush.setCurrentColor(new SavableColor(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity()));
        });
    }

    private void initializeIncreaseSizeComboBox() {
        this.sizePicker = new ComboBox<>();
        sizePicker.getItems().addAll(CanvasToolbar.increaseSizeValues);
        sizePicker.setValue(this.userBrush.getCurrentWidth());
        sizePicker.setOnAction((event) -> this.userBrush.setCurrentWidth(this.sizePicker.getValue()));
    }

    private void updateSize() {
        currentSize.setText("Current Line Width: " + this.userBrush.getCurrentWidth());
    }

    public boolean equals(CanvasToolbar other) {
        return other.userBrush == this.userBrush;
    }

    @Override
    public boolean equals(Object other) {
        boolean equal = false;
        if (getClass().isInstance(other)) equal = equals(this.getClass().cast(other));
        return equal;
    }

    @Override
    public int hashCode() {
        return this.userBrush.hashCode();
    }
}
