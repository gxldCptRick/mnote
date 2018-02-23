package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class DrawableCanvas extends VBox {

	private Canvas mainDrawingCanvas;
	private Color currentColor;
	private double lineWidth;
	private double lineWidthIncrease;
	private Label currentSize;

	private EventHandler<MouseEvent>[] drawableMouseEvents;
	private List<List<Point2D>> lines;
	private List<Point2D> currentLine;

	@SuppressWarnings("unchecked")
	public DrawableCanvas(double width, double height) {
		super();
		this.setSpacing(5);
		lines = new ArrayList<>();
		currentLine = new ArrayList<>();
		drawableMouseEvents = new EventHandler[3];
		
		lineWidth = .5;
		lineWidthIncrease = .1;
		currentSize = new Label("Current Line Width : " + lineWidth);
		currentColor = Color.BLACK;
		
		mainDrawingCanvas = generateCanvas(width, height);
		setUpToolBar();
		
		
		
		ScrollPane canvasContainer = new ScrollPane();
		canvasContainer.setContent(mainDrawingCanvas);
	
		
		canvasContainer.addEventFilter(InputEvent.ANY, (event)-> {
		    if (event.getEventType().toString() == "SCROLL")
		        event.consume();
		});
		
		this.getChildren().add(canvasContainer);
	}

	private Canvas generateCanvas(double width, double height) {
		Canvas canvas = new Canvas(width, height);
		
		setUpMouseEvents(canvas);
		setUpDrawing(canvas);
		
		return canvas;
	}

	private void setUpToolBar() {

		ColorPicker colorList = generateColorPicker();

		ComboBox<Double> increaseSizes = generateIncreaseSizeComboBox();

		HBox toolBar = new HBox();

		toolBar.setSpacing(10);
		
		toolBar.getChildren().addAll(currentSize, colorList, increaseSizes);

		setButtons(toolBar);
		
		this.getChildren().add(toolBar);
	}

	private void setButtons(HBox toolBar) {
		
		Button eraserButton = new Button("Clear");
		eraserButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mainDrawingCanvas.getGraphicsContext2D().clearRect(0, 0, mainDrawingCanvas.getWidth(),
						mainDrawingCanvas.getHeight());

			}

		});

		Button increaseLineSize = new Button("+"), decreaseLineSize = new Button("-");

		increaseLineSize.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				lineWidth += lineWidthIncrease;

				updateSize();

			}

		});

		decreaseLineSize.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				lineWidth -= lineWidthIncrease;
				updateSize();
			}

		});

		toolBar.getChildren().addAll(eraserButton, increaseLineSize, decreaseLineSize);

	}

	private ColorPicker generateColorPicker() {

		ColorPicker colorList = new ColorPicker();
		colorList.setValue(Color.BLACK);
		colorList.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Object source = event.getSource();

				if (ColorPicker.class.isInstance(source)) {

					ColorPicker picker = ColorPicker.class.cast(source);

					currentColor = picker.getValue();

					System.out.println(picker.getValue());

				}

			}

		});

		return colorList;
	}

	private ComboBox<Double> generateIncreaseSizeComboBox() {

		ComboBox<Double> increaseSizes = new ComboBox<Double>();

		increaseSizes.getItems().addAll(Arrays.asList(.1, .2, .3, .4, .5, .6, .7, .8, .9, 1.0, 10.0, 100.0));
		increaseSizes.setValue(.1);
		increaseSizes.setOnAction(new EventHandler<ActionEvent>() {

			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent event) {

				ComboBox<Double> comboBox = (ComboBox<Double>) event.getSource();

				lineWidthIncrease = comboBox.getValue();

			}

		});

		return increaseSizes;
	}

	private void setUpDrawing(Canvas canvas) {

		canvas.setOnMouseDragged(drawableMouseEvents[0]);

		canvas.setOnMouseReleased(drawableMouseEvents[1]);

		canvas.setOnMousePressed(drawableMouseEvents[2]);

	}

	private void setUpMouseEvents(Canvas canvas) {
		drawableMouseEvents[0] = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				currentLine = new ArrayList<>();

				GraphicsContext gc = canvas.getGraphicsContext2D();

				currentLine.add(new Point2D(event.getX(), event.getY()));

				gc.lineTo(event.getX(), event.getY());

				gc.stroke();
			}

		};

		drawableMouseEvents[1] = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				GraphicsContext gc = canvas.getGraphicsContext2D();
				currentLine.add(new Point2D(event.getX(), event.getY()));
				gc.lineTo(event.getX(), event.getY());
				gc.closePath();

				lines.add(currentLine);
			}

		};

		drawableMouseEvents[2] = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				GraphicsContext gc = canvas.getGraphicsContext2D();
				gc.beginPath();
				gc.setLineWidth(lineWidth);
				gc.setStroke(currentColor);
				currentLine.add(new Point2D(event.getX(), event.getY()));
				gc.moveTo(event.getX(), event.getY());
			}

		};

	}

	private void updateSize() {

		currentSize.setText("Current Line Width: " + lineWidth);
	}

}
