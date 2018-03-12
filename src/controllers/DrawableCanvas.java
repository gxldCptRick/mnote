package controllers;

import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.StrokeLineCap;
import models.CanvasLines;
import models.NoteData;
import models.SavablePoint2D;
import models.enums.SpecialEffect;
import views.CanvasToolbar;

public class DrawableCanvas implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID;
	private static final double MAX_WIDTH;
	private static final double MAX_HEIGHT;

	static {

		serialVersionUID = generateID("420 BLAZE IT");
		MAX_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 3;
		MAX_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 3;

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
	private List<NoteData> notes;

	private transient VBox layout;
	private transient Canvas mainDrawingCanvas;
	private transient ScrollPane canvasContainer;
	private transient EventHandler<MouseEvent>[] drawableMouseEvents;
	private transient Group canvasGroup;

	public DrawableCanvas(double width, double height) {

		initialize(width, height);

	}

	public Pane getLayout() {

		return this.layout;

	}

	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {

		ObjectInputStream.GetField fields = in.readFields();

		this.lines = (CanvasLines) fields.get("lines", null);

		this.toolbar = (CanvasToolbar) fields.get("toolbar", null);

		this.notes = (List<NoteData>) fields.get("notes", null);

		initialize(Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
				Toolkit.getDefaultToolkit().getScreenSize().getHeight());

		lines.drawLines(this.mainDrawingCanvas.getGraphicsContext2D());

	}

	private void writeObject(ObjectOutputStream out) throws IOException {

		out.defaultWriteObject();

	}

	private void initialize(double width, double height) {

		initializeLayout();

		initializeMouseEvents();

		initializeNotes();

		initializeCanvas(width, height);

		initializeLines();

		initializeToolbar();

		initializeScrollPane();

		layout.getChildren().addAll(toolbar.getLayout(), canvasContainer);

	}

	private void initializeNotes() {

		this.notes = notes == null ? new ArrayList<>() : notes;

	}

	private void initializeLayout() {

		if (layout == null)
			layout = new VBox();

		if (this.canvasGroup == null)
			canvasGroup = new Group();

		layout.setSpacing(5);

	}

	@SuppressWarnings("unchecked")
	private void initializeMouseEvents() {

		if (drawableMouseEvents == null)
			drawableMouseEvents = new EventHandler[3];

		this.setUpMouseEvents();

	}

	private void initializeLines() {

		if (lines == null)
			lines = new CanvasLines();

	}

	private void initializeToolbar() {

		if (toolbar == null)
			toolbar = new CanvasToolbar();

		
		MenuItem clearCanvas = new MenuItem("Whiteboard");
		
		MenuItem clearDrawing = new MenuItem("Drawings");
		
		MenuItem clearAnnotations = new MenuItem("Annotations");
		
		toolbar.getContextMenu().getItems().addAll(clearCanvas, clearDrawing, clearAnnotations);
		
		clearCanvas.setOnAction(event -> {
			
			this.clearAnnotations();
			this.clearCanvas();
			
		});
		
		clearDrawing.setOnAction(event -> {
			
			this.clearCanvas();
			
		});
		
		clearAnnotations.setOnAction(event -> {
			
			this.clearAnnotations();
			
		});
		
	}

	private void clearAnnotations() {
		
		this.canvasGroup = new Group();
		
		this.notes = new ArrayList<>();
		
		this.canvasGroup.getChildren().add(this.mainDrawingCanvas);
		
		this.mainDrawingCanvas.setOnMouseClicked(event -> {
			
			if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 2) {

				NoteData note = new NoteData(event.getX(), event.getY());

				this.notes.add(note);

				TextField input = note.getInputForText();
				List<Node> children = canvasGroup.getChildren();

				children.add(input);

				input.requestFocus();

				input.setOnAction(inputEntered -> {

					note.updateText();

					children.add(note.getDisplayForText());
					children.remove(input);

					note.getDisplayForText().setOnMouseClicked(clicked -> {

						children.remove(note.getDisplayForText());

						children.add(input);

						input.requestFocus();
					});

				});
			}

		});
		
		this.canvasContainer.setContent(this.canvasGroup);
		
	}

	private void initializeScrollPane() {

		canvasContainer = new ScrollPane();

		this.canvasGroup.getChildren().add(mainDrawingCanvas);

		canvasContainer.setContent(canvasGroup);

		setupCanvasGroup();

		setupScrollPaneSize();

		setupScrollPaneMousePanEvents();

		setupScrollPaneEventFilters();

	}

	private void setupCanvasGroup() {
		if (notes.size() > 0) {

			List<Node> children = canvasGroup.getChildren();
			for (NoteData noteData : notes) {
				if (noteData != null) {

					TextField note = noteData.getInputForText();
					note.setOnAction(inputEntered -> {

						noteData.updateText();

						children.add(noteData.getDisplayForText());
						children.remove(note);

					});

					noteData.getDisplayForText().setOnMouseClicked(clicked -> {

						children.remove(noteData.getDisplayForText());

						children.add(note);

						note.requestFocus();
					});

					canvasGroup.getChildren().add(noteData.getDisplayForText());

				}
			}

		}
	}

	private void initializeCanvas(double width, double height) {
		this.mainDrawingCanvas = new Canvas(width, height);

		setUpDrawing();
		setupNoteClicked();

		this.mainDrawingCanvas.setOnMouseClicked(event -> {
			if(toolbar.isDelete())
				checkRemove(event);
			
		});
		
	}

	private void setupNoteClicked() {

		this.mainDrawingCanvas.setOnMouseClicked(event -> {

			if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 2) {

				NoteData note = new NoteData(event.getX(), event.getY());

				this.notes.add(note);

				TextField input = note.getInputForText();
				List<Node> children = canvasGroup.getChildren();

				children.add(input);

				input.requestFocus();

				input.setOnAction(inputEntered -> {

					note.updateText();

					children.add(note.getDisplayForText());
					children.remove(input);

					note.getDisplayForText().setOnMouseClicked(clicked -> {

						children.remove(note.getDisplayForText());

						children.add(input);

						input.requestFocus();
					});

				});
			}

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

		canvasContainer.setOnMouseDragged(event -> {

			if (event.getX() + 100 > canvasContainer.getWidth()) {

				this.canvasContainer.setHvalue(calculateJump(canvasContainer.getHvalue(),
						canvasContainer.getViewportBounds().getWidth(), mainDrawingCanvas.getWidth(), 1));
			} else if (event.getX() < 10) {

				this.canvasContainer.setHvalue(calculateJump(canvasContainer.getHvalue(),
						canvasContainer.getViewportBounds().getWidth(), mainDrawingCanvas.getWidth(), -1));

			}

			if (event.getY() + 10 > canvasContainer.getViewportBounds().getHeight()) {

				this.canvasContainer.setVvalue(calculateJump(canvasContainer.getVvalue(),
						canvasContainer.getViewportBounds().getHeight(), mainDrawingCanvas.getHeight(), 1));

			} else if (event.getY() < 10) {

				this.canvasContainer.setVvalue(calculateJump(canvasContainer.getVvalue(),
						canvasContainer.getViewportBounds().getHeight(), mainDrawingCanvas.getHeight(), -1));

			}

		});

	}

	private void setupScrollPaneSize() {

		canvasContainer.prefWidthProperty().bind(layout.prefWidthProperty().multiply(.75));

		canvasContainer.prefHeightProperty().bind(layout.prefHeightProperty().multiply(.75));

	}

	private void setUpMouseEvents() {

		drawableMouseEvents[0] = (event) -> {

			if (!toolbar.isDelete()) {
				if (event != null && event.isPrimaryButtonDown()) {

					checkIfInboundsOfView(event);

					checkIfInboundsOfCanvas(event);

					GraphicsContext gc = mainDrawingCanvas.getGraphicsContext2D();

					lines.addNextPoint(new SavablePoint2D(event.getX(), event.getY()));

					gc.lineTo(event.getX(), event.getY());

					gc.stroke();
				}
			}

		};

		drawableMouseEvents[1] = (event) -> {

			if (!toolbar.isDelete()) {

				if (event != null && lines.isLineStarted()) {

					GraphicsContext gc = mainDrawingCanvas.getGraphicsContext2D();

					lines.addNextPoint(new SavablePoint2D(event.getX(), event.getY()));

					gc.lineTo(event.getX(), event.getY());

					gc.stroke();

					gc.closePath();
				}

			}
		};

		drawableMouseEvents[2] = (event) -> {
			if (!toolbar.isDelete()) {
				if (event != null && event.isPrimaryButtonDown()) {

					GraphicsContext gc = mainDrawingCanvas.getGraphicsContext2D();
					gc.beginPath();

					if (this.toolbar.isSpecial()) {

						GaussianBlur blur = new GaussianBlur();
						gc.setEffect(blur);
						lines.startNewLine(this.toolbar.getCurrentColor(), this.toolbar.getLineWidth(),
								SpecialEffect.GuassianBlur);
					} else {

						lines.startNewLine(this.toolbar.getCurrentColor(), this.toolbar.getLineWidth());
						gc.setEffect(null);

					}
					
					gc.setLineWidth(toolbar.getLineWidth());
					gc.setStroke(toolbar.getCurrentColor());
					gc.setLineCap(StrokeLineCap.ROUND);
					gc.moveTo(event.getX(), event.getY());
					gc.stroke();

					lines.addNextPoint(new SavablePoint2D(event.getX(), event.getY()));

				}

			} 
		};

	}

	private void checkRemove(MouseEvent event) {
		
		boolean removed = lines.removeLine(new Point2D(event.getX(), event.getY()));
		
		if (removed) {

			
			clearCanvas();
			
			this.lines.drawLines(mainDrawingCanvas.getGraphicsContext2D());

			
		}
	}

	private void clearCanvas() {
		this.lines = new CanvasLines();
		
		GraphicsContext gc = mainDrawingCanvas.getGraphicsContext2D();
		
		gc.setEffect(null);
		gc.clearRect(0, 0, mainDrawingCanvas.getWidth(),
				mainDrawingCanvas.getHeight());
	}

	private void setUpDrawing() {

		this.mainDrawingCanvas.setOnMouseDragged(drawableMouseEvents[0]);

		this.mainDrawingCanvas.setOnMouseReleased(drawableMouseEvents[1]);

		this.mainDrawingCanvas.setOnMousePressed(drawableMouseEvents[2]);

	}

	private double calculateJump(double currentSize, double sizeOfView, double sizeOfObject, double direction) {

		double jumpSize = currentSize + (sizeOfView / sizeOfObject) * .1 * direction;

		return jumpSize;
	}

	private void checkIfInboundsOfCanvas(MouseEvent event) {

		if (event.getX() + 10 > mainDrawingCanvas.getWidth()) {

			mainDrawingCanvas.setWidth(MAX_WIDTH);
		}

		if (event.getY() + 10 > mainDrawingCanvas.getHeight()) {

			mainDrawingCanvas.setHeight(MAX_HEIGHT);
		}

	}

	private void checkIfInboundsOfView(MouseEvent event) {

		Event.fireEvent(canvasContainer, event);

	}

}
