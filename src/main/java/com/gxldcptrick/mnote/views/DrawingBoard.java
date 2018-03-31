package com.gxldcptrick.mnote.views;

import java.awt.Toolkit;
import java.awt.image.RenderedImage;

import com.gxldcptrick.mnote.models.Brush;
import com.gxldcptrick.mnote.models.CanvasLines;
import com.gxldcptrick.mnote.models.SavablePoint2D;
import com.gxldcptrick.mnote.models.enums.SpecialEffect;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
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
    private NoteGroup canvasGroup;
    private Canvas canvas;

    public DrawingBoard(double width, double height) {

        this.initialize(width, height);

    }

    public Brush getCanvasBrush() {

        return this.canvasBrush;
    }

    public RenderedImage captureImage() {

        WritableImage image = new WritableImage((int) canvas.getWidth(),
                (int) canvas.getHeight());

        canvas.snapshot(null, image);

        RenderedImage img = SwingFXUtils.fromFXImage(image, null);

        return img;
    }

    public void clearAnnotations() {

        this.canvasGroup = new NoteGroup();

        this.canvasGroup.getChildren().add(this.canvas);

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

        this.canvasGroup.getChildren().add(canvas);

        this.setContent(this.canvasGroup);

        setupScrollPaneMousePanEvents();

    }

    private void initializeCanvas(double width, double height) {
        this.canvas = new Canvas(width, height);

        setUpDrawing();
        setupCanvasClickEvents();

    }

    private void setUpDrawing() {

        this.canvas.setOnMouseDragged((event) -> {

            if ((event != null && event.isPrimaryButtonDown()) && !this.canvasBrush.isDeleting()) {

                checkIfInboundsOfCanvas(event);

                drawLine(event);
            }

        });

        this.canvas.setOnMouseReleased((event) -> {

            if ((event != null && lines.isLineStarted()) && !canvasBrush.isDeleting()) {

                drawLine(event);

                this.canvas.getGraphicsContext2D().closePath();

            }
        });

        this.canvas.setOnMousePressed((event) -> {

            if ((event != null && event.isPrimaryButtonDown()) && !canvasBrush.isDeleting()) {

                startLine(event);

            }
        });

    }

    private void startLine(MouseEvent event) {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        configureGraphics(gc);

        lines.addNextPoint(new SavablePoint2D(event.getX(), event.getY()));

        gc.moveTo(event.getX(), event.getY());

        gc.stroke();
    }

    private void drawLine(MouseEvent event) {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        lines.addNextPoint(new SavablePoint2D(event.getX(), event.getY()));

        gc.lineTo(event.getX(), event.getY());

        gc.stroke();

    }

    private void setupCanvasClickEvents() {

        this.canvas.setOnMouseClicked(event -> {

            if (this.canvasBrush.isDeleting()) {

                checkRemove(event);

            } else if (event.getButton() == MouseButton.PRIMARY) {

                GraphicsContext gc = canvas.getGraphicsContext2D();

                configureGraphics(gc);

                gc.strokeOval(event.getX(), event.getY(), canvasBrush.getCurrentWidth(), canvasBrush.getCurrentWidth());

                gc.closePath();

                this.lines.addNextPoint(new SavablePoint2D(event.getX(), event.getY()));
            }


        });

    }


    private void setupScrollPaneMousePanEvents() {

        this.addEventFilter(InputEvent.ANY, (event) -> {
            if (event.getEventType().toString().equals("SCROLL"))
                event.consume();
        });

        this.setOnMousePressed(event -> {

            if (event.isSecondaryButtonDown())
                this.setPannable(true);

        });

        this.setOnMouseReleased(event ->

                this.setPannable(false));

        this.setOnMouseDragged(event -> {

            calculateHorizontalBound(event);
            calculateVerticalBounds(event);

        });

    }

    private void calculateVerticalBounds(MouseEvent event) {

        if (event.getY() + 10 > this.getViewportBounds().getHeight()) {

            this.setVvalue(calculateJump(this.getVvalue(), this.getViewportBounds().getHeight(),
                    canvas.getHeight(), 1));

        } else if (event.getY() < 10) {

            this.setVvalue(calculateJump(this.getVvalue(), this.getViewportBounds().getHeight(),
                    canvas.getHeight(), -1));

        }

    }

    private void calculateHorizontalBound(MouseEvent event) {
        if (event.getX() + 100 > this.getWidth()) {

            this.setHvalue(calculateJump(this.getHvalue(), this.getViewportBounds().getWidth(),
                    canvas.getWidth(), 1));
        } else if (event.getX() < 10) {

            this.setHvalue(calculateJump(this.getHvalue(), this.getViewportBounds().getWidth(),
                    canvas.getWidth(), -1));

        }
    }


    private void clearDrawings() {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setEffect(null);
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    }

    private void checkRemove(MouseEvent event) {

        boolean removed = lines.removeLine(new Point2D(event.getX(), event.getY()));

        if (removed) {

            clearDrawings();

            this.lines.drawLines(canvas.getGraphicsContext2D());

            System.out.println(lines);

        }
    }

    private void checkIfInboundsOfCanvas(MouseEvent event) {

        Event.fireEvent(this, event);

        if (event.getX() + 10 > canvas.getWidth()) {

            canvas.setWidth(MAX_CANVAS_WIDTH);
        }

        if (event.getY() + 10 > canvas.getHeight()) {

            canvas.setHeight(MAX_CANVAS_HEIGHT);
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

    }

}
