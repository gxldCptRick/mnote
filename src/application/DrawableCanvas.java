package application;

import java.awt.Toolkit;
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

	@SuppressWarnings("unused")
	private static final double MAX_WIDTH;
	@SuppressWarnings("unused")
	private static final double MAX_HEIGHT;

	static {
		MAX_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 2;
		MAX_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 2;
	}

	private Canvas mainDrawingCanvas;
	private Color currentColor;
	private double lineWidth;
	private double lineWidthIncrease;
	private Label currentSize;
	private ScrollPane canvasContainer;

	private EventHandler<MouseEvent>[] drawableMouseEvents;
	private List<List<Point2D>> lines;
	private List<Point2D> currentLine;

	@SuppressWarnings("unchecked")
	public DrawableCanvas(double width, double height) {
		super();

		System.out.println(width+ " w," + height + " h");
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

	}

	private void initializeScrollPane() {
		canvasContainer = new ScrollPane();
		
		canvasContainer.setContent(mainDrawingCanvas);
		
		setupScrollPaneSize();
		
		setupScrollPaneMousePanEvents();
		
		setupScrollPaneEventFilters();
		
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

		canvasContainer.prefWidthProperty().bind(this.prefWidthProperty().multiply(.9));

		canvasContainer.prefHeightProperty().bind(this.prefHeightProperty().multiply(.8));

	}

	private Canvas generateCanvas(double width, double height) {
		Canvas canvas = new Canvas(width, height);

		setUpMouseEvents(canvas);
		setUpDrawing(canvas);
		setUpReDraw(canvas);

		return canvas;
	}

	private void setUpReDraw(Canvas canvas) {

		canvas.widthProperty().addListener((listener) -> redraw());
		canvas.heightProperty().addListener((listener) -> redraw());

	}

	private void redraw() {

		GraphicsContext gc = mainDrawingCanvas.getGraphicsContext2D();

		gc.clearRect(0, 0, mainDrawingCanvas.getWidth(), mainDrawingCanvas.getHeight());

		boolean first = true;

		for (List<Point2D> thing : lines) {
			gc.beginPath();

			for (Point2D point2d : thing) {
				if (first) {
					first = false;

					gc.moveTo(point2d.getX(), point2d.getY());

				} else {

					gc.lineTo(point2d.getX(), point2d.getY());

				}
			}
			first = true;
			gc.stroke();

			gc.closePath();

		}

		gc.beginPath();
		for (Point2D point2d : currentLine) {
			gc.lineTo(point2d.getX(), point2d.getY());
		}
		gc.stroke();
		gc.closePath();

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

			if (ColorPicker.class.isInstance(source)) {

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

			@SuppressWarnings("unchecked")
			ComboBox<Double> comboBox = (ComboBox<Double>) event.getSource();

			lineWidthIncrease = comboBox.getValue();

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
				checkIfInbound(event);

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

	private void checkIfInbound(MouseEvent event) {
		
/*		System.out.println(event.getX() + " , " + event.getY());
		System.out.println(this.canvasContainer.getViewportBounds().getHeight() + " viewport height ");
*/
		System.out.println(this.canvasContainer.getVvalue() * this.mainDrawingCanvas.getHeight() + " vertical * height");
		
	}

	private void updateSize() {

		currentSize.setText("Current Line Width: " + lineWidth);
	}

}
