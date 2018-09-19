package com.gxldcptrick.mnote.FXView.views;

import java.awt.image.RenderedImage;
import java.io.*;

import com.gxldcptrick.mnote.network.*;
import com.gxldcptrick.mnote.FXView.models.*;
import com.gxldcptrick.mnote.FXView.enums.SpecialEffect;

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

public class DrawingBoard extends ScrollPane implements Serializable {
    private static ClientSocket socket;

    private CanvasLines lines;
    private Brush canvasBrush;
    private transient NoteGroup noteGroup;
    private transient Canvas userCanvas;

    public static ClientSocket getSocket() {
        return socket;
    }

    public static void SetClientSocket(ClientSocket socket) {
        DrawingBoard.socket = socket;
    }

    public DrawingBoard(double width, double height) {
        this.initialize(width, height);
    }

    public Brush getCanvasBrush() {
        return this.canvasBrush;
    }

    public void saveData(ObjectOutputStream out) throws IOException {
        out.writeObject(this.lines);
        out.writeObject(this.canvasBrush);
        out.writeDouble(userCanvas.getWidth());
        out.writeDouble(userCanvas.getHeight());
        noteGroup.saveData(out);
    }

    public void reloadData(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.lines = (CanvasLines) in.readObject();
        this.canvasBrush = (Brush) in.readObject();
        double width = in.readDouble();
        double height = in.readDouble();
        this.noteGroup.reloadData(in);
        initialize(width, height);
        this.lines.drawLines(this.userCanvas.getGraphicsContext2D());
    }

    public RenderedImage captureImage() {
        WritableImage image = new WritableImage((int) userCanvas.getWidth(), (int) userCanvas.getHeight());
        this.snapshot(null, image);
        RenderedImage img = SwingFXUtils.fromFXImage(image, null);
        return img;
    }

    public void clearAnnotations() {
        this.noteGroup = new NoteGroup();
        this.noteGroup.getChildren().add(this.userCanvas);
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
        this.noteGroup.getChildren().add(userCanvas);
        this.setContent(this.noteGroup);
        setupScrollPaneMousePanEvents();
    }

    private void initializeCanvas(double width, double height) {
        this.userCanvas = new Canvas(width, height);
        setUpDrawing();
        setupCanvasClickEvents();
    }

    private void setUpDrawing() {
        this.userCanvas.setOnMouseDragged((event) -> {
            if (event != null) {
                if ((event.isPrimaryButtonDown()) && !this.canvasBrush.isDeleting()) {
                    checkIfInboundsOfCanvas(event);
                }
                drawLine(event);
            }
        });
        this.userCanvas.setOnMouseReleased((event) -> {
            if ((event != null && lines.isLineStarted()) && !canvasBrush.isDeleting()) {
                drawLine(event);
                this.userCanvas.getGraphicsContext2D().closePath();
            }
        });
        this.userCanvas.setOnMousePressed((event) -> {
            if ((event != null && event.isPrimaryButtonDown()) && !canvasBrush.isDeleting()) {
                System.out.println("Starting Line");
                startLine(event);
            }
        });
    }

    public void killSockets() {
        if (socket != null && socket.isAlive())
            socket.killConnection();
    }

    private void startLine(MouseEvent event) {
        SavablePoint2D savablePoint2D = new SavablePoint2D(event.getX(), event.getY());
        DrawingPackage aPackage = new DrawingPackage(savablePoint2D, event.getEventType(), getCanvasBrush(), true, false);
        System.out.println(aPackage.getMouseEvent());
        System.out.println(event.getEventType());
        GraphicsContext gc = userCanvas.getGraphicsContext2D();
        configureGraphics(gc);
        lines.addNextPoint(savablePoint2D);
        gc.moveTo(event.getX(), event.getY());
        gc.stroke();
    }

    private void drawLine(MouseEvent event) {
        SavablePoint2D savablePoint2D = new SavablePoint2D(event.getX(), event.getY());
        DrawingPackage aPackage = new DrawingPackage(savablePoint2D, event.getEventType(), getCanvasBrush(), false, false);
        GraphicsContext gc = userCanvas.getGraphicsContext2D();
        lines.addNextPoint(savablePoint2D);
        gc.lineTo(event.getX(), event.getY());
        gc.stroke();
    }

    private void setupCanvasClickEvents() {
        this.userCanvas.setOnMouseClicked(event -> {
            if (this.canvasBrush.isDeleting()) {
                checkRemove(event);
            } else if (event.getButton() == MouseButton.PRIMARY) {
                GraphicsContext gc = userCanvas.getGraphicsContext2D();
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
        this.setOnMouseReleased(event -> this.setPannable(false));
        this.setOnMouseDragged(event -> {
            calculateHorizontalBound(event);
            calculateVerticalBounds(event);
        });
    }

    private void calculateVerticalBounds(MouseEvent event) {
        if (event.getY() + 10 > this.getViewportBounds().getHeight()) {
            this.setVvalue(
                    calculateJump(this.getVvalue(), this.getViewportBounds().getHeight(), userCanvas.getHeight(), 1));
        } else if (event.getY() < 10) {
            this.setVvalue(
                    calculateJump(this.getVvalue(), this.getViewportBounds().getHeight(), userCanvas.getHeight(), -1));
        }
    }

    private void calculateHorizontalBound(MouseEvent event) {
        if (event.getX() + 100 > this.getWidth()) {
            this.setHvalue(
                    calculateJump(this.getHvalue(), this.getViewportBounds().getWidth(), userCanvas.getWidth(), 1));
        } else if (event.getX() < 10) {
            this.setHvalue(
                    calculateJump(this.getHvalue(), this.getViewportBounds().getWidth(), userCanvas.getWidth(), -1));
        }
    }

    private void clearDrawings() {
        GraphicsContext gc = userCanvas.getGraphicsContext2D();
        gc.setEffect(SpecialEffect.None.lineEffect);
        gc.clearRect(0, 0, userCanvas.getWidth(), userCanvas.getHeight());
    }

    private void checkRemove(MouseEvent event) {
        boolean removed = lines.removeLine(new Point2D(event.getX(), event.getY()));
        if (removed) {
            clearDrawings();
            this.lines.drawLines(userCanvas.getGraphicsContext2D());
            System.out.println(lines);
        }
    }

    private double calculateJump(double currentSize, double sizeOfView, double sizeOfObject, double direction) {
        return currentSize + (sizeOfView / sizeOfObject) * .1 * direction;
    }

    private void checkIfInboundsOfCanvas(MouseEvent event) {
        Event.fireEvent(this, event);
        if (event.getX() + 10 > userCanvas.getWidth())
            userCanvas.setWidth(userCanvas.getWidth() * 2);
        if (event.getY() + 10 > userCanvas.getHeight())
            userCanvas.setHeight(this.getHeight() * 2);

    }

    private void configureGraphics(GraphicsContext gc) {
        configureGraphics(gc, this.canvasBrush);
    }

    private void configureGraphics(GraphicsContext gc, Brush canvasBrush) {
        gc.beginPath();
        lines.startNewLine(canvasBrush.getCurrentColor().getColor(), canvasBrush.getCurrentWidth(), canvasBrush.getEffect());
        gc.setLineWidth(canvasBrush.getCurrentWidth());
        gc.setStroke(canvasBrush.getCurrentColor().getColor());
        gc.setLineCap(canvasBrush.getBrushCap());
        var effect = canvasBrush.getEffect();
        gc.setEffect(effect.lineEffect);
    }
}