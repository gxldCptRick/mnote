package com.gxldcptrick.mnote.views;

import java.awt.Toolkit;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;

import com.gxldcptrick.mnote.models.Brush;
import com.gxldcptrick.mnote.models.CanvasLines;
import com.gxldcptrick.mnote.models.NoteData;
import com.gxldcptrick.mnote.models.SavablePoint2D;
import com.gxldcptrick.mnote.models.enums.SpecialEffect;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class DrawingBoard extends ScrollPane {

	private static final double MAX_CANVAS_WIDTH;
	private static final double MAX_CANVAS_HEIGHT;

	static {

		MAX_CANVAS_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 3;
		MAX_CANVAS_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 1.5;

	}

	private CanvasLines lines;
	private Brush canvasBrush;

	private Canvas mainDrawingCanvas;
	private NoteGroup canvasGroup;

	public DrawingBoard(double width, double height) {

		this.initialize(width, height);

	}

	public Brush getCanvasBrush() {

		return this.canvasBrush;
	}

	public RenderedImage captureImage() {

		WritableImage image = new WritableImage((int) mainDrawingCanvas.getWidth(),
				(int) mainDrawingCanvas.getHeight());

		mainDrawingCanvas.snapshot(null, image);

		RenderedImage img  = SwingFXUtils.fromFXImage(image, null);
		
		return img;
	}
	
	public void clearAnnotations() {

		this.canvasGroup = new NoteGroup();

		this.canvasGroup.getChildren().add(this.mainDrawingCanvas);

		this.mainDrawingCanvas.setOnMouseClicked(this::mouseClicks);

		this.setContent(this.canvasGroup);

	}
	
	
	public void clearBoard() {

		this.clearAnnotations();
		this.clearLines();

	}
	
	public void clearLines() {
		clearDrawings();
		this.lines = new CanvasLines();

	}
	
	private void initialize(double width, double height) {

		if (this.canvasBrush == null)
			this.canvasBrush = new Brush();

		if (this.canvasGroup == null)
			canvasGroup = new NoteGroup();

		this.initializeLines();
		this.initializeCanvas(width, height);
		this.initializeLayout();

	}



	private void initializeLines() {

		if (lines == null)
			lines = new CanvasLines();

	}

	

	private void initializeLayout() {

		this.canvasGroup.getChildren().add(mainDrawingCanvas);

		this.setContent(this.canvasGroup);

		setupScrollPaneMousePanEvents();

		setupScrollPaneEventFilters();

	}
	
	private void initializeCanvas(double width, double height) {
		this.mainDrawingCanvas = new Canvas(width, height);

		setUpDrawing();
		setupNoteClicked();

	}

	private void setUpDrawing() {

		this.mainDrawingCanvas.setOnMouseDragged((event) -> {

			if (!this.canvasBrush.isDeleting()) {
				if (event != null && event.isPrimaryButtonDown()) {

					checkIfInboundsOfView(event);

					checkIfInboundsOfCanvas(event);

					GraphicsContext gc = mainDrawingCanvas.getGraphicsContext2D();

					lines.addNextPoint(new SavablePoint2D(event.getX(), event.getY()));

					gc.lineTo(event.getX(), event.getY());

					gc.stroke();
				}
			}

		});

		this.mainDrawingCanvas.setOnMouseReleased((event) -> {

			if (!canvasBrush.isDeleting()) {

				if (event != null && lines.isLineStarted()) {

					GraphicsContext gc = mainDrawingCanvas.getGraphicsContext2D();

					lines.addNextPoint(new SavablePoint2D(event.getX(), event.getY()));

					gc.lineTo(event.getX(), event.getY());

					gc.stroke();

					gc.closePath();
				}

			}
		});

		this.mainDrawingCanvas.setOnMousePressed((event) -> {
			if (!canvasBrush.isDeleting()) {
				if (event != null && event.isPrimaryButtonDown()) {
					GraphicsContext gc = mainDrawingCanvas.getGraphicsContext2D();
					configureGraphics(gc);
					lines.addNextPoint(new SavablePoint2D(event.getX(), event.getY()));
					gc.moveTo(event.getX(), event.getY());
					gc.stroke();

				}

			}
		});

	}

	private void setupNoteClicked() {

		this.mainDrawingCanvas.setOnMouseClicked(this::mouseClicks);

	}

    private void mouseClicks(MouseEvent event) {

        if (this.canvasBrush.isDeleting()){

            checkRemove(event);

        } else if (event.getButton() == MouseButton.PRIMARY) {

            GraphicsContext gc = mainDrawingCanvas.getGraphicsContext2D();

            configureGraphics(gc);

            gc.strokeRect(event.getX(), event.getY(), canvasBrush.getCurrentWidth(),
                    canvasBrush.getCurrentWidth());

            gc.closePath();
        }

        Event.fireEvent(this.canvasGroup, event);

    }

    private void setupScrollPaneEventFilters() {

		this.addEventFilter(InputEvent.ANY, (event) -> {
			if (event.getEventType().toString().equals("SCROLL"))
				event.consume();
		});

	}

	private void setupScrollPaneMousePanEvents() {
		this.setOnMousePressed(event -> {

			if (event.isSecondaryButtonDown())
				this.setPannable(true);

		});

		this.setOnMouseReleased(event ->

		this.setPannable(false));

		this.setOnMouseDragged(event -> {

			if (event.getX() + 100 > this.getWidth()) {

				this.setHvalue(calculateJump(this.getHvalue(), this.getViewportBounds().getWidth(),
						mainDrawingCanvas.getWidth(), 1));
			} else if (event.getX() < 10) {

				this.setHvalue(calculateJump(this.getHvalue(), this.getViewportBounds().getWidth(),
						mainDrawingCanvas.getWidth(), -1));

			}

			if (event.getY() + 10 > this.getViewportBounds().getHeight()) {

				this.setVvalue(calculateJump(this.getVvalue(), this.getViewportBounds().getHeight(),
						mainDrawingCanvas.getHeight(), 1));

			} else if (event.getY() < 10) {

				this.setVvalue(calculateJump(this.getVvalue(), this.getViewportBounds().getHeight(),
						mainDrawingCanvas.getHeight(), -1));

			}

		});

	}


	private void clearDrawings() {

		GraphicsContext gc = mainDrawingCanvas.getGraphicsContext2D();

		gc.setEffect(null);
		gc.clearRect(0, 0, mainDrawingCanvas.getWidth(), mainDrawingCanvas.getHeight());

	}

	private void checkRemove(MouseEvent event) {

		boolean removed = lines.removeLine(new Point2D(event.getX(), event.getY()));

		if (removed) {

			clearDrawings();

			this.lines.drawLines(mainDrawingCanvas.getGraphicsContext2D());

			System.out.println(lines);

		}
	}
	
	private void checkIfInboundsOfView(MouseEvent event) {

		Event.fireEvent(this, event);

	}

	private void checkIfInboundsOfCanvas(MouseEvent event) {

		if (event.getX() + 10 > mainDrawingCanvas.getWidth()) {

			mainDrawingCanvas.setWidth(MAX_CANVAS_WIDTH);
		}

		if (event.getY() + 10 > mainDrawingCanvas.getHeight()) {

			mainDrawingCanvas.setHeight(MAX_CANVAS_HEIGHT);
		}

	}

	private double calculateJump(double currentSize, double sizeOfView, double sizeOfObject, double direction) {

		return currentSize + (sizeOfView / sizeOfObject) * .1 * direction;
	}

	
	private void configureGraphics(GraphicsContext gc) {

		gc.beginPath();

		lines.startNewLine(this.canvasBrush.getCurrentColor(), this.canvasBrush.getCurrentWidth(),
				this.canvasBrush.getEffect());

		gc.setLineWidth(canvasBrush.getCurrentWidth());
		gc.setStroke(canvasBrush.getCurrentColor());
		gc.setLineCap(canvasBrush.getBrushCap());
		SpecialEffect effect = this.canvasBrush.getEffect();

		if (effect == null) {

			gc.setEffect(null);

		} else {

			gc.setEffect(this.canvasBrush.getEffect().lineEffect);

		}
		lines.startNewLine();

	}

}
