package com.gxldcptrick.mnote.controllers;

import com.gxldcptrick.mnote.FXView.enums.SpecialEffect;
import com.gxldcptrick.mnote.events.CanvasToolbarEvents;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

public class CanvasToolBarController {
    @FXML
    ColorPicker colorPicker;
    @FXML
    ComboBox<Double> lineWidthComboBox;
    @FXML
    Button clearBtn;
    @FXML
    ComboBox<SpecialEffect> specialEffects;
    @FXML
    CheckBox deleting;

    private static final List<Double> increaseSizeValues;

    static {
        increaseSizeValues = Arrays.asList(.1, .2, .3, .4, .5, .6, .7, .8, .9, 1.0, 10.0, 100.0);
    }

    @FXML
    public void initialize(){
        specialEffects.getItems().addAll(SpecialEffect.values());
        specialEffects.setValue(SpecialEffect.None);

        lineWidthComboBox.getItems().addAll(increaseSizeValues);
        lineWidthComboBox.setValue(1.0);

        colorPicker.setValue(Color.BLACK);

        JavaFxObservable.valuesOf(specialEffects.valueProperty())
                .subscribe(CanvasToolbarEvents.getInstance().getChangeSpecialEfects());

        JavaFxObservable.valuesOf(lineWidthComboBox.valueProperty())
                .subscribe(CanvasToolbarEvents.getInstance().getChangedLineSize());

        JavaFxObservable.valuesOf(colorPicker.valueProperty())
                .subscribe(CanvasToolbarEvents.getInstance().getChangedColor());

        JavaFxObservable.actionEventsOf(clearBtn)
                .subscribe(CanvasToolbarEvents.getInstance().getClearCanvas());

        JavaFxObservable.actionEventsOf(deleting)
                .subscribe(CanvasToolbarEvents.getInstance().getDeletingLine());

    }
}
