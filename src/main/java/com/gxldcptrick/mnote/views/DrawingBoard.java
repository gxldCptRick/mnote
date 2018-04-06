package com.gxldcptrick.mnote.views;

import java.awt.Toolkit;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gxldcptrick.mnote.controllers.ClientSocket;
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

public class DrawingBoard extends ScrollPane implements Serializable{

    private static final double MAX_CANVAS_WIDTH;
    private static final double MAX_CANVAS_HEIGHT;

    static {

        MAX_CANVAS_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 3;
        MAX_CANVAS_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 1.5;

    }

    private CanvasLines lines;
    private Brush canvasBrush;
    private NoteGroup noteGroup;
    private Canvas canvas;

    private static ClientSocket socket;

    public DrawingBoard() {

    }
    public DrawingBoard(double width, double height) {

        this.initialize(width, height);
        socket = new ClientSocket(this);
        socket.start();

    }

    public Brush getCanvasBrush() {

        return this.canvasBrush;
    }

    public void saveData(ObjectOutputStream out) throws IOException {


        out.writeObject(lines);
        out.writeObject(canvasBrush);
        noteGroup.saveData(out);
        out.writeDouble(canvas.getWidth());
        out.writeDouble(canvas.getHeight());

    }

    public void reloadData(ObjectInputStream in) throws IOException, ClassNotFoundException {

        this.lines = (CanvasLines) in.readObject();
        this.canvasBrush = (Brush) in.readObject();
        this.noteGroup.reloadData(in);
        double width = in.readDouble();
        double height = in.readDouble();

        initialize(width, height);

    }

    public RenderedImage captureImage() {

        WritableImage image = new WritableImage((int) canvas.getWidth(),
                (int) canvas.getHeight());

        canvas.snapshot(null, image);

        RenderedImage img = SwingFXUtils.fromFXImage(image, null);

        return img;
    }

    public void clearAnnotations() {

        this.noteGroup = new NoteGroup();

        this.noteGroup.getChildren().add(this.canvas);

        this.setContent(this.noteGroup);

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

        if (this.noteGroup == null)
            noteGroup = new NoteGroup();

        this.initializeLines();
        this.initializeCanvas(width, height);
        this.initializeLayout();

    }

    private void initializeLines() {

        if (lines == null)
            lines = new CanvasLines();

    }

    private void initializeLayout() {

        this.noteGroup.getChildren().add(canvas);

        this.setContent(this.noteGroup);

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

    private  void startLine(MouseEvent event) {
        SavablePoint2D savablePoint2D = new SavablePoint2D(event.getX(), event.getY());

        socket.sendObject(savablePoint2D);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        configureGraphics(gc);

        lines.addNextPoint(savablePoint2D);

        gc.moveTo(event.getX(), event.getY());

        gc.stroke();
    }

    public void startLine(SavablePoint2D points) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        configureGraphics(gc);

        lines.addNextPoint(points);

        gc.stroke();
    }

    public void drawLine(SavablePoint2D point2D) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        lines.addNextPoint(point2D);

        gc.lineTo(point2D.get2DPoint().getX(), point2D.get2DPoint().getY());

        gc.stroke();
    }

    private void drawLine(MouseEvent event) {

        SavablePoint2D savablePoint2D = new SavablePoint2D(event.getX(), event.getY());
        socket.sendObject(savablePoint2D);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        lines.addNextPoint(savablePoint2D);

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

        lines.startNewLine(canvasBrush.getCurrentColor(), canvasBrush.getCurrentWidth(),
                canvasBrush.getEffect());

        gc.setLineWidth(canvasBrush.getCurrentWidth());
        gc.setStroke(canvasBrush.getCurrentColor());
        gc.setLineCap(canvasBrush.getBrushCap());

        SpecialEffect effect = canvasBrush.getEffect();

        if (effect == null) {

            gc.setEffect(null);

        } else {

            gc.setEffect(canvasBrush.getEffect().lineEffect);

        }

    }

}
