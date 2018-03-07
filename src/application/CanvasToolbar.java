package application;

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

public class CanvasToolbar implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 556677L;
	private static final List<Double> increaseSizeValues;

	static {

		increaseSizeValues = Arrays.asList(.1, .2, .3, .4, .5, .6, .7, .8, .9, 1.0, 10.0, 100.0);

	}

	private double lineWidth;
	private double lineWidthIncrease;

	@SuppressWarnings("unused")
	private double currentRed;
	@SuppressWarnings("unused")
	private double currentBlue;
	@SuppressWarnings("unused")
	private double currentGreen;

	private transient HBox layout;
	private transient Color currentColor;
	private transient Label currentSize;
	private transient Button clearButton;
	private transient Button increaseLineSize;
	private transient Button decreaseLineSize ;
	private transient ColorPicker colorPicker;
	private transient ComboBox<Double> sizePicker;

	public CanvasToolbar() {

		initializeToolbar();

	}

	public Pane getLayout() {

		return layout;

	}

	public double getLineWidth() {

		return this.lineWidth;

	}

	public Color getCurrentColor() {

		return currentColor;

	}

	public Button getClearButton() {
		return clearButton;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {

		currentRed = this.currentColor.getRed();
		currentBlue = this.currentColor.getBlue();
		currentGreen = this.currentColor.getGreen();

		out.defaultWriteObject();

	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {

		initializeToolbar();

		ObjectInputStream.GetField fields = in.readFields();

		this.lineWidth = fields.get("lineWidth", .5);

		this.lineWidthIncrease = fields.get("lineWidthIncrease", 0.1);

		this.currentColor = new Color(fields.get("currentRed", 0d), fields.get("currentBlue", 0d),
				fields.get("currentGreen", 0d), 0d);

		this.colorPicker.setValue(this.currentColor);

		this.sizePicker.setValue(this.lineWidthIncrease);

		
	}

	private void initializeToolbar() {
		
		this.clearButton = new Button("Clear");
		this.increaseLineSize = new Button("+");
		this.decreaseLineSize = new Button("-");
		
		currentSize = new Label("Current Line Width : " + lineWidth);
		
		currentColor = Color.BLACK;
		
		layout = new HBox();

		this.lineWidth = .5;
		this.lineWidthIncrease = .1;

		this.updateSize();

		initializeColorPicker();

		initializeIncreaseSizeComboBox();

		setupButtons();

		layout.setSpacing(10);

		layout.getChildren().addAll(currentSize, colorPicker, sizePicker, clearButton, increaseLineSize,
				decreaseLineSize);

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

		sizePicker.setValue(.1);

		sizePicker.setOnAction((ActionEvent event) -> {

			Object source = event.getSource();
			if (source instanceof ComboBox<?>) {

				@SuppressWarnings("unchecked")
				ComboBox<Double> comboBox = (ComboBox<Double>) source;

				lineWidthIncrease = comboBox.getValue();

			}
		});

	}

	private void setupButtons() {

		increaseLineSize.setOnAction((ActionEvent event) -> {
			lineWidth += lineWidthIncrease;
			updateSize();
		});

		decreaseLineSize.setOnAction((ActionEvent event) -> {
			lineWidth -= lineWidthIncrease;
			updateSize();
		});
	}

	private void updateSize() {

		currentSize.setText("Current Line Width: " + lineWidth);

	}

}
