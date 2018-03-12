package views;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javafx.event.ActionEvent;
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

	private SavableColor lastColor;

	private transient HBox layout;
	private transient Color currentColor;
	private transient Label currentSize;
	private transient ContextMenu contextMenu;
	private transient Button eraseButton;
	private transient ColorPicker colorPicker;
	private transient ComboBox<Double> sizePicker;
	private transient CheckBox check;
	private transient CheckBox deletings;

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
	
	public ContextMenu getContextMenu() {
		
		return this.contextMenu;
	}

	
	private void initializeErase() {
		
		this.eraseButton.setOnAction(event -> {
			this.contextMenu.show(eraseButton, Side.RIGHT,0,0);
			
		});
		
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

		this.contextMenu = new ContextMenu();
		this.eraseButton = new Button("Erase");
		this.eraseButton.setContextMenu(contextMenu);
		this.check = new CheckBox("Add Special Effect");
		this.deletings = new CheckBox("Delete Line");
		this.initializeErase();
		
		currentSize = new Label("Current Line Width : " + currentLineWidth);

		currentColor = Color.BLACK;

		layout = new HBox();

		this.currentLineWidth = .5;

		this.updateSize();

		initializeColorPicker();

		initializeIncreaseSizeComboBox();

		layout.setSpacing(10);

		layout.getChildren().addAll(currentSize, colorPicker, sizePicker, this.eraseButton, this.check ,this.deletings);

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
				updateSize();

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
	
	public boolean isDelete() {
		
		return this.deletings.isSelected();
	}
	
	public boolean isSpecial() {
		
		return this.check.isSelected();
	}

	@Override
	public boolean equals(Object other) {
		boolean equal = false;

		if (other != null || CanvasToolbar.class.isInstance(other)) {

			equals(this.getClass().cast(other));

		}

		return equal;

	}

	@Override
	public int hashCode() {
		
		return currentColor.hashCode() ^ currentSize.hashCode();
	}
	

}
