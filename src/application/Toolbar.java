package application;

import java.io.Serializable;
import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Toolbar implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 556677L;
	private double lineWidth;
	private double lineWidthIncrease;
	
	private transient HBox  layout;
	private transient Color currentColor;
	private transient Label currentSize;
	private transient Button clearButton = new Button("Clear");
	private transient Button increaseLineSize = new Button("+");
	private transient Button decreaseLineSize = new Button("-");
	
	public Toolbar() {

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
	
	
	private void initializeToolbar() {
		currentSize = new Label("Current Line Width : " + lineWidth);
		currentColor = Color.BLACK;
		layout = new HBox();
		
		ColorPicker colorList = generateColorPicker();

		ComboBox<Double> increaseSizes = generateIncreaseSizeComboBox();

		layout.setSpacing(10);

		layout.getChildren().addAll(currentSize, colorList, increaseSizes);

		setButtons();

	}

	private void setButtons() {


		increaseLineSize.setOnAction((ActionEvent event) -> {
			lineWidth += lineWidthIncrease;
			updateSize();
		});

		decreaseLineSize.setOnAction((ActionEvent event) -> {
			lineWidth -= lineWidthIncrease;
			updateSize();
		});

		layout.getChildren().addAll(clearButton, increaseLineSize, decreaseLineSize);

	}

	private ColorPicker generateColorPicker() {

		ColorPicker colorList = new ColorPicker();
		colorList.setValue(Color.BLACK);
		colorList.setOnAction((event) -> {

			Object source = event.getSource();

			if (source instanceof ColorPicker) {

				ColorPicker picker = ColorPicker.class.cast(source);

				currentColor = picker.getValue();

				System.out.println(picker.getValue());
			}
		});

		return colorList;
	}

	private ComboBox<Double> generateIncreaseSizeComboBox() {

		ComboBox<Double> increaseSizes = new ComboBox<Double>();

		increaseSizes.getItems().addAll(Arrays.asList(.1, .2, .3, .4, .5, .6, .7, .8, .9, 1.0, 10.0, 100.0));

		increaseSizes.setValue(.1);

		increaseSizes.setOnAction((ActionEvent event) -> {

			Object sauce = event.getSource();
			if (sauce instanceof ComboBox<?>) {

				@SuppressWarnings("unchecked")
				ComboBox<Double> comboBox = (ComboBox<Double>) sauce;

				lineWidthIncrease = comboBox.getValue();

			}
		});

		return increaseSizes;
	}
	
	private void updateSize() {

		currentSize.setText("Current Line Width: " + lineWidth);
	}

}