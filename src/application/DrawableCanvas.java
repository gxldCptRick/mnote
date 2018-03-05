package application;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
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
	
	private static final double MAX_WIDTH;
	private static final double MAX_HEIGHT;

	private static final double STANDARD_DEVATION_Y;
	private static final double STANDARD_DEVATION_X;

	static {
		
		MAX_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 3;
		MAX_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 3;
		STANDARD_DEVATION_Y = .018311;
		STANDARD_DEVATION_X = .010121;
	
	}

	private Canvas mainDrawingCanvas;
	private Color currentColor;
	private double lineWidth;
	private double lineWidthIncrease;
	private Label currentSize;
	private ScrollPane canvasContainer;
	private double offsetY;
	private double offsetX;

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
		initializeScrollPane();

		updateOffsetY();
		updateOffsetX();

	}

	private void updateOffsetY() {

		offsetY = ((1 + STANDARD_DEVATION_Y) * mainDrawingCanvas.getHeight())
				- canvasContainer.getViewportBounds().getHeight();

	}

	private void updateOffsetX() {

		offsetX = ((1 + STANDARD_DEVATION_X) * mainDrawingCanvas.getWidth())
				- canvasContainer.getViewportBounds().getWidth();
		System.out.println(offsetX);
	}

	private void initializeScrollPane() {
		canvasContainer = new ScrollPane();

		canvasContainer.setContent(mainDrawingCanvas);

		setupScrollPaneSize();

		setupScrollPaneMousePanEvents();

		setupScrollPaneEventFilters();

		canvasContainer.widthProperty().addListener(event -> {
			updateOffsetX();
		});

		canvasContainer.heightProperty().addListener(event -> {

			updateOffsetY();

		});
	

		this.getChildren().add(canvasContainer);

	}

	private void setupScrollPaneEventFilters() {

		canvasContainer.addEventFilter(InputEvent.ANY, (event) -> {
			if (event.getEventType().toString() == "SCROLL")
				event.consume();
		});

	}

	private void setupScrollPaneMousePanEvents() {
		canvasContainer.setOnMousePressed(event -> {

			if (event.isSecondaryButtonDown())
				canvasContainer.setPannable(true);

		});

		canvasContainer.setOnMouseReleased(event -> {

			canvasContainer.setPannable(false);

		});

	}

	private void setupScrollPaneSize() {

		canvasContainer.prefWidthProperty().bind(this.prefWidthProperty().multiply(.75));

		canvasContainer.prefHeightProperty().bind(this.prefHeightProperty().multiply(.75));

	}

	private Canvas generateCanvas(double width, double height) {
		Canvas canvas = new Canvas(width, height);

		setUpMouseEvents(canvas);
		setUpDrawing(canvas);
		setBoundsUpdate(canvas);

		return canvas;
	}

	private void setBoundsUpdate(Canvas canvas) {

		canvas.widthProperty().addListener((listener) -> updateOffsetX());

		canvas.heightProperty().addListener((listener) -> {

			updateOffsetY();

		});

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

	private double calculateTrueYPosition(double originalYPos) {

		
		double yPos = originalYPos - Math.round(offsetY * canvasContainer.getVvalue()) + 1;
		System.out.println(offsetY * canvasContainer.getVvalue());
		return yPos;
	}

	private double calculateTrueXPosition(double originalXPos) {

		double xPos = originalXPos - Math.round(offsetX * canvasContainer.getHvalue()) + 1;
		
		System.out.println(offsetX * canvasContainer.getHvalue());
		
		return xPos;
	}

	private void setButtons(HBox toolBar) {

		Button clearButton = new Button("Clear"), increaseLineSize = new Button("+"),
				decreaseLineSize = new Button("-");

		clearButton.setOnAction((ActionEvent event) -> {
			this.lines = new ArrayList<>();
			this.currentLine = new ArrayList<>();
			mainDrawingCanvas.getGraphicsContext2D().clearRect(0, 0, mainDrawingCanvas.getWidth(),
					mainDrawingCanvas.getHeight());
		});

		increaseLineSize.setOnAction((ActionEvent event) -> {
			lineWidth += lineWidthIncrease;
			updateSize();
		});

		decreaseLineSize.setOnAction((ActionEvent event) -> {
			lineWidth -= lineWidthIncrease;
			updateSize();
		});

		toolBar.getChildren().addAll(clearButton, increaseLineSize, decreaseLineSize);

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

	private void setUpDrawing(Canvas canvas) {

		canvas.setOnMouseDragged(drawableMouseEvents[0]);

		canvas.setOnMouseReleased(drawableMouseEvents[1]);

		canvas.setOnMousePressed(drawableMouseEvents[2]);

	}

	private void setUpMouseEvents(Canvas canvas) {
		drawableMouseEvents[0] = (event) -> {
			if (event.isPrimaryButtonDown()) {

				checkIfInboundsOfView(event);

				checkIfInboundsOfCanvas(event);

				GraphicsContext gc = canvas.getGraphicsContext2D();

				currentLine.add(new Point2D(event.getX(), event.getY()));

				gc.lineTo(event.getX(), event.getY());

				gc.stroke();
			}

		};

		drawableMouseEvents[1] = (event) -> {

			if (currentLine != null && !lines.contains(currentLine)) {
				GraphicsContext gc = canvas.getGraphicsContext2D();
				currentLine.add(new Point2D(event.getX(), event.getY()));
				gc.lineTo(event.getX(), event.getY());
				gc.closePath();

				lines.add(currentLine);
			}
		};

		drawableMouseEvents[2] = (event) -> {

			if (event.isPrimaryButtonDown()) {
				// System.out.println(event.getX() + " event x");
				// System.out.println(this.canvasContainer.getHvalue() + " horizontal value");
				currentLine = new ArrayList<>();
				GraphicsContext gc = canvas.getGraphicsContext2D();
				gc.beginPath();
				gc.setLineWidth(lineWidth);
				gc.setStroke(currentColor);
				currentLine.add(new Point2D(event.getX(), event.getY()));
				gc.moveTo(event.getX(), event.getY());

			}

		};

	}

	private void checkIfInboundsOfCanvas(MouseEvent event) {

		if (event.getX() > mainDrawingCanvas.getWidth()) {

			mainDrawingCanvas.setWidth(MAX_WIDTH);
		}

		if (event.getY() > mainDrawingCanvas.getHeight()) {

			mainDrawingCanvas.setHeight(MAX_HEIGHT);
		}

	}

	private void checkIfInboundsOfView(MouseEvent event) {

		double x = calculateTrueXPosition(event.getX()), y = calculateTrueYPosition(event.getY());
		Bounds viewPortBounds = canvasContainer.getViewportBounds();

		System.out.printf("the current x value is %.1f the current y value is %.1f\n", x, y);
		System.out.printf("the current viewport width is %.1f and the current viewport height is %.1f\n",
				viewPortBounds.getWidth(), viewPortBounds.getHeight());

		if (x + 10 > viewPortBounds.getWidth()) {

			this.canvasContainer.setHvalue(calculateJump(canvasContainer.getHvalue(), viewPortBounds.getWidth(),
					mainDrawingCanvas.getWidth(), 1));

		} else if (x < 10) {

			this.canvasContainer.setHvalue(calculateJump(canvasContainer.getHvalue(), viewPortBounds.getWidth(),
					mainDrawingCanvas.getWidth(), -1));

		}

		if (y + 10 > viewPortBounds.getHeight()) {

			this.canvasContainer.setVvalue(calculateJump(canvasContainer.getVvalue(), viewPortBounds.getHeight(),
					mainDrawingCanvas.getHeight(), 1));

		} else if (y < 10) {

			this.canvasContainer.setVvalue(calculateJump(canvasContainer.getVvalue(), viewPortBounds.getHeight(),
					mainDrawingCanvas.getHeight(), -1));

		}

	}

	private double calculateJump(double currentSize, double sizeOfView, double sizeOfObject, double direction) {

		double jumpSize = currentSize + (sizeOfView / sizeOfObject) * .1 * direction;

		return jumpSize;
	}

	private void updateSize() {

		currentSize.setText("Current Line Width: " + lineWidth);
	}

}
