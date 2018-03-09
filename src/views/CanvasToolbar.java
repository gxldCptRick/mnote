package views;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import models.SavableColor;

public class CanvasToolbar implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 556677L;
	private static final List<Double> increaseSizeValues;

	static {

		increaseSizeValues = Arrays.asList(.1, .2, .3, .4, .5, .6, .7, .8, .9, 1.0, 10.0, 100.0);

	}

	private double currentLineWidth;

	@SuppressWarnings("unused")
	private SavableColor lastColor;

	private transient HBox layout;
	private transient Color currentColor;
	private transient Label currentSize;
	private transient Button clearButton;
	private transient ColorPicker colorPicker;
	private transient ComboBox<Double> sizePicker;

	public CanvasToolbar() {

		initializeToolbar();

	}

	public Pane getLayout() {

		return layout;

	}

	public double getLineWidth() {

		return this.currentLineWidth;

	}

	public Color getCurrentColor() {

		return currentColor;

	}

	public Button getClearButton() {
		return clearButton;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {

		this.lastColor = new SavableColor(this.currentColor);

		out.defaultWriteObject();

	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {

		initializeToolbar();

		ObjectInputStream.GetField fields = in.readFields();

		this.currentLineWidth = fields.get("currentLineWidth", .5);

		this.lastColor = (SavableColor) fields.get("lastColor", null);
		
		if (lastColor != null)
			this.currentColor = this.lastColor.getColor();

		this.colorPicker.setValue(this.currentColor);

		this.sizePicker.setValue(this.currentLineWidth);

	}

	private void initializeToolbar() {

		this.clearButton = new Button("Clear Drawing");

		currentSize = new Label("Current Line Width : " + currentLineWidth);

		currentColor = Color.BLACK;

		layout = new HBox();

		this.currentLineWidth = .5;

		this.updateSize();

		initializeColorPicker();

		initializeIncreaseSizeComboBox();

		layout.setSpacing(10);

		layout.getChildren().addAll(currentSize, colorPicker, sizePicker, clearButton);

	}

	private void initializeColorPicker() {

		this.colorPicker = new ColorPicker();

		colorPicker.setValue(Color.BLACK);

		colorPicker.setOnAction((event) -> {

			Object source = event.getSource();

			if (source instanceof ColorPicker) {

				ColorPicker picker = ColorPicker.class.cast(source);

				currentColor = picker.getValue();

				System.out.println(picker.getValue());
			}
		});

	}

	private void initializeIncreaseSizeComboBox() {

		this.sizePicker = new ComboBox<Double>();

		sizePicker.getItems().addAll(CanvasToolbar.increaseSizeValues);

		sizePicker.setValue(this.currentLineWidth);

		sizePicker.setOnAction((ActionEvent event) -> {

			Object source = event.getSource();
			if (source instanceof ComboBox<?>) {

				@SuppressWarnings("unchecked")
				ComboBox<Double> comboBox = (ComboBox<Double>) source;

				currentLineWidth = comboBox.getValue();

			}
		});

	}

	private void updateSize() {

		currentSize.setText("Current Line Width: " + currentLineWidth);

	}

	public boolean equals(CanvasToolbar other) {

		return other != null && other.currentColor == this.currentColor
				&& other.currentSize.getText().equals(this.currentSize.getText());

	}

	@Override
	public boolean equals(Object other) {
		boolean equal = false;

		if (other != null || CanvasToolbar.class.isInstance(other)) {

			equals(this.getClass().cast(other));

		}

		return equal;

	}

}
