package application;

import java.awt.Toolkit;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import models.CanvasLines;
import models.SavablePoint2D;

public class DrawableCanvas implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID;
	private static final double MAX_WIDTH;
	private static final double MAX_HEIGHT;

	private static final double STANDARD_DEVATION_Y;
	private static final double STANDARD_DEVATION_X;

	static {

		serialVersionUID = generateID("420 BLAZE IT");
		MAX_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 3;
		MAX_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 3;

		STANDARD_DEVATION_Y = .018310648;
		STANDARD_DEVATION_X = .00796156;

	}

	private static long generateID(String input) {
		
		long counter = 0;
		char[] inputs = input.toCharArray();
		for (int i = 0; i <  inputs.length; i++) {

			counter += inputs[i] + i;

		}

		return counter;
	}

	private CanvasLines lines;
	private Toolbar toolbar;
	
	private transient final VBox layout;
	private transient Canvas mainDrawingCanvas;
	private transient ScrollPane canvasContainer;
	private transient double offsetY;
	private transient double offsetX;

	private transient EventHandler<MouseEvent>[] drawableMouseEvents;

	@SuppressWarnings("unchecked")
	public DrawableCanvas(double width, double height) {

		layout = new VBox();
		layout.setSpacing(5);
		lines = new CanvasLines();
		toolbar = new Toolbar();
		
		drawableMouseEvents = new EventHandler[3];

		mainDrawingCanvas = generateCanvas(width, height);

	
		initializeScrollPane();

		updateOffsetY();
		updateOffsetX();

		layout.getChildren().addAll(toolbar.getLayout(), canvasContainer);

	}

	public Pane getLayout() {
		return this.layout;
	}

	private void updateOffsetY() {

		offsetY = ((1 + STANDARD_DEVATION_Y) * mainDrawingCanvas.getHeight()) - canvasContainer.getHeight();

	}

	private void updateOffsetX() {

		offsetX = ((1 + STANDARD_DEVATION_X) * mainDrawingCanvas.getWidth()) - canvasContainer.getWidth();
		System.out.println("Offset x updated");
		System.out.println(offsetX);
		System.out.println(canvasContainer.getWidth());
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

		canvasContainer.prefWidthProperty().bind(layout.prefWidthProperty().multiply(.75));

		canvasContainer.prefHeightProperty().bind(layout.prefHeightProperty().multiply(.75));

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

	

	private double calculateTrueYPosition(double originalYPos) {

		double yPos = originalYPos - Math.round(offsetY * canvasContainer.getVvalue()) + 1;

		return yPos;
	}

	private double calculateTrueXPosition(double originalXPos) {

		double xPos = originalXPos - Math.round(offsetX * canvasContainer.getHvalue()) + 1;

		System.out.println(originalXPos);
		System.out.println(offsetX);
		System.out.println(Math.round(offsetX * canvasContainer.getHvalue()) + 1d);
		System.out.println(canvasContainer.getHvalue());
		return xPos;
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

				lines.addNextPoint(new SavablePoint2D(event.getX(), event.getY()));

				gc.lineTo(event.getX(), event.getY());

				gc.stroke();
			}

		};

		drawableMouseEvents[1] = (event) -> {

			if (lines.isLineStarted()) {

				GraphicsContext gc = canvas.getGraphicsContext2D();

				lines.addNextPoint(new SavablePoint2D(event.getX(), event.getY()));

				gc.lineTo(event.getX(), event.getY());

				gc.closePath();

				lines.endLine();

			}
		};

		drawableMouseEvents[2] = (event) -> {

			if (event.isPrimaryButtonDown()) {

				lines.startNewLine();
				GraphicsContext gc = canvas.getGraphicsContext2D();
				gc.beginPath();
				gc.setLineWidth(toolbar.getLineWidth());
				gc.setStroke(toolbar.getCurrentColor());
				lines.addNextPoint(new SavablePoint2D(event.getX(), event.getY()));
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

}
