package application;

import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
		for (int i = 0; i < inputs.length; i++) {

			counter += inputs[i] + i;

		}

		return counter;
	}

	private CanvasLines lines;
	private CanvasToolbar toolbar;

	private transient VBox layout;
	private transient Canvas mainDrawingCanvas;
	private transient ScrollPane canvasContainer;
	private transient double offsetY;
	private transient double offsetX;
	private transient EventHandler<MouseEvent>[] drawableMouseEvents;

	public DrawableCanvas(double width, double height) {


		initialize(width, height);
		

	}

	public Pane getLayout() {
	
		return this.layout;
	
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		
		System.out.println("taking out");
		
			initialize(Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		
			ObjectInputStream.GetField fields = in.readFields();
			
			this.lines = (CanvasLines) fields.get("lines", new CanvasLines());
			
			this.toolbar = (CanvasToolbar) fields.get("toolbar", new CanvasToolbar());
			
			
			System.out.println(mainDrawingCanvas);
			System.out.println(mainDrawingCanvas.getGraphicsContext2D());
			this.lines.drawLines(this.mainDrawingCanvas.getGraphicsContext2D());
			
			
	}
	
	
	private void writeObject(ObjectOutputStream out) throws IOException{

		out.defaultWriteObject();
		
	}

	@SuppressWarnings("unchecked")
	private void initialize(double width, double height) {

		layout = new VBox();

		layout.setSpacing(5);
		lines = new CanvasLines();
		toolbar = new CanvasToolbar();

		drawableMouseEvents = new EventHandler[3];

		initializeCanvas(width, height);

		toolbar.getClearButton().setOnAction((event) -> {

			lines = new CanvasLines();

			mainDrawingCanvas.getGraphicsContext2D().clearRect(0, 0, mainDrawingCanvas.getWidth(),
					mainDrawingCanvas.getHeight());

		});

		initializeScrollPane();

		
		this.layout.getChildren().addAll(toolbar.getLayout(), canvasContainer);
		
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

	private void initializeCanvas(double width, double height) {
		this.mainDrawingCanvas = new Canvas(width, height);

		setUpMouseEvents();
		setUpDrawing();
		setBoundsUpdate();

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

	private void setUpMouseEvents() {
		drawableMouseEvents[0] = (event) -> {
			if (event.isPrimaryButtonDown()) {

				checkIfInboundsOfView(event);

				checkIfInboundsOfCanvas(event);

				GraphicsContext gc = mainDrawingCanvas.getGraphicsContext2D();

				lines.addNextPoint(new SavablePoint2D(event.getX(), event.getY()));

				gc.lineTo(event.getX(), event.getY());

				gc.stroke();
			}

		};

		drawableMouseEvents[1] = (event) -> {

			if (lines.isLineStarted()) {

				GraphicsContext gc = mainDrawingCanvas.getGraphicsContext2D();

				lines.addNextPoint(new SavablePoint2D(event.getX(), event.getY()));

				gc.lineTo(event.getX(), event.getY());

				gc.closePath();

			}
		};

		drawableMouseEvents[2] = (event) -> {

			if (event.isPrimaryButtonDown()) {

				GraphicsContext gc = mainDrawingCanvas.getGraphicsContext2D();

				gc.beginPath();
				gc.setLineWidth(toolbar.getLineWidth());
				gc.setStroke(toolbar.getCurrentColor());
				gc.moveTo(event.getX(), event.getY());

				lines.startNewLine();
				lines.addNextPoint(new SavablePoint2D(event.getX(), event.getY()));

			}

		};

	}

	private void setUpDrawing() {

		this.mainDrawingCanvas.setOnMouseDragged(drawableMouseEvents[0]);

		this.mainDrawingCanvas.setOnMouseReleased(drawableMouseEvents[1]);

		this.mainDrawingCanvas.setOnMousePressed(drawableMouseEvents[2]);

	}

	private void setBoundsUpdate() {

		this.mainDrawingCanvas.widthProperty().addListener((listener) -> updateOffsetX());

		this.mainDrawingCanvas.heightProperty().addListener((listener) -> {

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

	private double calculateJump(double currentSize, double sizeOfView, double sizeOfObject, double direction) {

		double jumpSize = currentSize + (sizeOfView / sizeOfObject) * .1 * direction;

		return jumpSize;
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

}
