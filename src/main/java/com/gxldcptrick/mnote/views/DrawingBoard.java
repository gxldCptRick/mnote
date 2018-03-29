package com.gxldcptrick.mnote.views;

import com.gxldcptrick.mnote.models.Brush;
import com.gxldcptrick.mnote.models.CanvasLines;
import com.gxldcptrick.mnote.models.NoteData;
import com.gxldcptrick.mnote.models.SavablePoint2D;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.WritableImage;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;

public class  DrawingBoard extends ScrollPane{

    private static final double MAX_CANVAS_WIDTH;
    private static final double MAX_CANVAS_HEIGHT;

    static {

        MAX_CANVAS_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 3;
        MAX_CANVAS_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 1.5;

    }


    private List<NoteData> notes;
    private CanvasLines lines;

    private BooleanProperty usingSpecialTip;
    private BooleanProperty deleting;

    private Canvas mainDrawingCanvas;
    private EventHandler<MouseEvent>[] drawableMouseEvents;
    private Group canvasGroup;


    public DrawingBoard(double width, double height){

        this.initialize(width, height);

    }

    private void initialize(double width, double height) {

        this.deleting = new SimpleBooleanProperty(false);
        this.usingSpecialTip = new SimpleBooleanProperty(false);

        if (this.canvasGroup == null)
            canvasGroup = new Group();

        this.initializeLines();
        this.initializeMouseEvents();
        this.initializeCanvas(width, height);
        this.initializeNotes();
        this.initializeScrollPane();


    }

    private void initializeNotes() {

        this.notes = notes == null ? new ArrayList<>() : notes;

    }

    private void initializeMouseEvents() {


        if (drawableMouseEvents == null)
            drawableMouseEvents = new EventHandler[3];

        this.setUpMouseEvents();

    }

    private void initializeLines() {

        if (lines == null)
            lines = new CanvasLines();

    }

    public RenderedImage captureImage(){

        WritableImage image = new WritableImage((int) mainDrawingCanvas.getWidth(),
                (int) mainDrawingCanvas.getHeight());

        mainDrawingCanvas.snapshot(null, image);

        return SwingFXUtils.fromFXImage(image, null);
    }


    public void clearAnnotations() {

        this.canvasGroup = new Group();

        this.notes = new ArrayList<>();

        this.canvasGroup.getChildren().add(this.mainDrawingCanvas);

        this.mainDrawingCanvas.setOnMouseClicked(event -> {

            if (deleting.get()) {
                checkRemove(event);
            }

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

        this.setContent(this.canvasGroup);

    }

    private void initializeScrollPane() {

        this.canvasGroup.getChildren().add(mainDrawingCanvas);

        this.setContent(this.canvasGroup);

        setupCanvasGroup();

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

    }

    private void setUpDrawing() {

        this.mainDrawingCanvas.setOnMouseDragged(drawableMouseEvents[0]);

        this.mainDrawingCanvas.setOnMouseReleased(drawableMouseEvents[1]);

        this.mainDrawingCanvas.setOnMousePressed(drawableMouseEvents[2]);

    }

    private void setupNoteClicked() {

        this.mainDrawingCanvas.setOnMouseClicked(event -> {
            if (deleting.get())
                checkRemove(event);

            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 2) {

                NoteData note = new NoteData(event.getX(), event.getY());

                this.notes.add(note);

                TextField input = note.getInputForText();
                List<Node> children = canvasGroup.getChildren();

                children.add(input);

                input.requestFocus();

                input.setOnAction(inputEntered -> {

                    note.updateText();
                    Label displayedText = note.getDisplayForText();

                    children.add(displayedText);
                    children.remove(input);
                    displayedText.setOnMouseClicked(clicked -> {

                        children.remove(displayedText);

                        children.add(input);

                        input.requestFocus();
                    });

                });
            } else if (event.getButton() == MouseButton.PRIMARY) {

                GraphicsContext gc = mainDrawingCanvas.getGraphicsContext2D();

                configureGraphics(gc);

//                gc.strokeRect(event.getX(), event.getY(), this.toolbar.getLineWidth(), this.toolbar.getLineWidth());
                gc.closePath();
            }

        });

    }

    public void clearBoard(){

        this.clearAnnotations();
        this.clearDrawings();
        this.lines = new CanvasLines();

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

            this.setPannable(false)
        );

        this.setOnMouseDragged(event -> {

            if (event.getX() + 100 > this.getWidth()) {

                this.setHvalue(calculateJump(this.getHvalue(),
                        this.getViewportBounds().getWidth(), mainDrawingCanvas.getWidth(), 1));
            } else if (event.getX() < 10) {

                this.setHvalue(calculateJump(this.getHvalue(),
                        this.getViewportBounds().getWidth(), mainDrawingCanvas.getWidth(), -1));

            }

            if (event.getY() + 10 > this.getViewportBounds().getHeight()) {

                this.setVvalue(calculateJump(this.getVvalue(),
                        this.getViewportBounds().getHeight(), mainDrawingCanvas.getHeight(), 1));

            } else if (event.getY() < 10) {

                this.setVvalue(calculateJump(this.getVvalue(),
                        this.getViewportBounds().getHeight(), mainDrawingCanvas.getHeight(), -1));

            }

        });

    }

    private void setUpMouseEvents() {

        drawableMouseEvents[0] = (event) -> {

            if (!deleting.get()) {
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

            if (!deleting.get()) {

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
            if (!deleting.get()) {
                if (event != null && event.isPrimaryButtonDown()) {
                    System.out.println(event.getX()+", " + event.getY() + " internal");
                    GraphicsContext gc = mainDrawingCanvas.getGraphicsContext2D();
                    configureGraphics(gc);
                    lines.addNextPoint(new SavablePoint2D(event.getX(), event.getY()));
                    gc.moveTo(event.getX(), event.getY());
                    gc.stroke();

                }

            }
        };

    }

    private void configureGraphics(GraphicsContext gc) {

        gc.beginPath();

        if (usingSpecialTip.get()) {

            GaussianBlur blur = new GaussianBlur();
            gc.setEffect(blur);
          //  lines.startNewLine(this.toolbar.getCurrentColor(), this.toolbar.getLineWidth(), SpecialEffect.GuassianBlur);
        } else {

            //lines.startNewLine(this.toolbar.getCurrentColor(), this.toolbar.getLineWidth());
            gc.setEffect(null);

        }

        //gc.setLineWidth(toolbar.getLineWidth());
        //gc.setStroke(toolbar.getCurrentColor());
        //gc.setLineCap(StrokeLineCap.ROUND);

        lines.startNewLine();

    }

    public Brush getABrush(){

        return new Brush(this.mainDrawingCanvas.getGraphicsContext2D());
    }


    public void clearLines(){
        clearDrawings();
        this.lines = new CanvasLines();

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

        }
    }

    private double calculateJump(double currentSize, double sizeOfView, double sizeOfObject, double direction) {

        return  currentSize + (sizeOfView / sizeOfObject) * .1 * direction;
    }

    private void checkIfInboundsOfCanvas(MouseEvent event) {


            if (event.getX() + 10 > mainDrawingCanvas.getWidth()) {

                mainDrawingCanvas.setWidth(MAX_CANVAS_WIDTH);
            }

            if (event.getY() + 10 > mainDrawingCanvas.getHeight()) {

                mainDrawingCanvas.setHeight(MAX_CANVAS_HEIGHT);
            }


    }

    private void checkIfInboundsOfView(MouseEvent event) {

        Event.fireEvent(this, event);

    }


}

