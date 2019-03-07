package com.gxldcptrick.mnote.components;

import com.gxldcptrick.mnote.enums.SpecialEffect;
import com.gxldcptrick.mnote.events.CanvasToolbarEvents;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CanvasToolbar extends HBox {
    private List<Node> components;
    private ColorPicker colorPicker;
    private ComboBox<Double> lineWidthComboBox;
    private Button clearBtn;
    private ComboBox<SpecialEffect> specialEffectsComboBox;
    private CheckBox deleting;

    private static final List<Double> lineWidthSizes;

    static {
        lineWidthSizes = Arrays.asList(.1, .2, .3, .4, .5, .6, .7, .8, .9, 1.0, 10.0, 100.0);
    }

    public CanvasToolbar() {
        components = new ArrayList<>();
        initializeColorPicker();
        initializeLineWidth();
        initializeLineWidthComboBox();
        initializeClearButton();
        initializeSpecialEffectsComboBox();
        initializeDeleting();

        setUpToolBarEvents();

        setSpacing(10);
        getChildren().addAll(components);
    }

    private void initializeLineWidth() {
        components.add(new Label("Current Line Width: "));
    }

    private void initializeColorPicker() {
        this.colorPicker = new ColorPicker();
        this.colorPicker.setValue(Color.BLACK);
        components.add(this.colorPicker);
    }

    private void initializeLineWidthComboBox() {
        this.lineWidthComboBox = new ComboBox<>();
        this.lineWidthComboBox.getItems().addAll(lineWidthSizes);
        this.lineWidthComboBox.setValue(1.0);
        components.add(this.lineWidthComboBox);
    }

    private void initializeClearButton() {
        this.clearBtn = new Button("Clear");
        components.add(this.clearBtn);
    }

    private void initializeSpecialEffectsComboBox() {
        this.specialEffectsComboBox = new ComboBox<>();
        this.specialEffectsComboBox.getItems().addAll(SpecialEffect.values());
        this.specialEffectsComboBox.setValue(SpecialEffect.None);
        components.add(this.specialEffectsComboBox);
    }

    private void initializeDeleting() {
        this.deleting = new CheckBox("Deleting");
        components.add(this.deleting);
    }

    private void setUpToolBarEvents() {
        JavaFxObservable.valuesOf(colorPicker.valueProperty())
                .subscribe(CanvasToolbarEvents.getInstance().getChangedColor());

        JavaFxObservable.valuesOf(lineWidthComboBox.valueProperty())
                .subscribe(CanvasToolbarEvents.getInstance().getChangedLineSize());

        JavaFxObservable.valuesOf(specialEffectsComboBox.valueProperty())
                .subscribe(CanvasToolbarEvents.getInstance().getChangeSpecialEfects());

        JavaFxObservable.actionEventsOf(clearBtn)
                .subscribe(CanvasToolbarEvents.getInstance().getClearCanvas());

        JavaFxObservable.actionEventsOf(deleting)
                .subscribe(CanvasToolbarEvents.getInstance().getDeletingLine());
    }
}
