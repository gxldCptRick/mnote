package com.gxldcptrick.mnote.FXView.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.gxldcptrick.mnote.FXView.enums.SpecialEffect;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

public class CanvasLine implements Serializable {

    private static final long serialVersionUID = 7230L;
    private final List<SavablePoint2D> points;
    private final Brush brush;

    //<editor-fold desc = "Constructor">

    public CanvasLine() {
        this((SavableColor) null, .5, SpecialEffect.None);
    }

    public CanvasLine(CanvasLine currentLine) {
        this(currentLine.brush);
        this.points.addAll(currentLine.points);
    }
    public CanvasLine(Brush brush){
        this.brush = brush;
        this.points = new ArrayList<>();
    }

    public CanvasLine(SavableColor color, double lineWidth, SpecialEffect effect) {
        this(new Brush(lineWidth, StrokeLineCap.ROUND, false,effect, color.getColor()));
    }
    public CanvasLine(Color color, double lineWidth){
        this(color, lineWidth, SpecialEffect.None);
    }

    public CanvasLine(Color colorOfLine, double lineWidth, SpecialEffect specialEffect) {
        this(new SavableColor(colorOfLine.getRed(), colorOfLine.getGreen(), colorOfLine.getBlue(), colorOfLine.getOpacity()), lineWidth, specialEffect);
    }
    //</editor-fold>

    public SavablePoint2D getInitialPoint() {
        SavablePoint2D point = null;

        if (points.size() > 0) {
            point = points.get(0);
        }
        return point;
    }

    public List<SavablePoint2D> getPoints() {
        return new ArrayList<>(this.points);

    }

    public void addNextPoint(SavablePoint2D nextPoint) {
        if (nextPoint != null)
            points.add(nextPoint);

    }

    public boolean contains(Point2D point) {
        boolean found = points.stream().filter(savedPoint -> savedPoint.get2DPoint().distance(point) < 15).count() > 0;
        return found;
    }

    public void drawLine(GraphicsContext gc) {
        if (this.points.size() > 1) {
            gc.beginPath();
            setUpGraphics(gc);
            SavablePoint2D startingPoint = getInitialPoint();

            if (startingPoint != null) {
                Point2D initialPoint = startingPoint.get2DPoint();
                gc.moveTo(initialPoint.getX(), initialPoint.getY());

                points.forEach(savedPoint -> {
                    if ((savedPoint != null) && (savedPoint != startingPoint)) {
                        Point2D point = savedPoint.get2DPoint();

                        gc.lineTo(point.getX(), point.getY());
                        gc.stroke();
                    }
                });
            }

            gc.closePath();
        } else if (points.size() == 1) {
            SavablePoint2D initialPoint = points.get(0);
            if (initialPoint != null) {
                Point2D dot = initialPoint.get2DPoint();
                gc.beginPath();
                setUpGraphics(gc);
                gc.strokeOval(dot.getX(), dot.getY(), );
                gc.closePath();
            }
        }
    }

    private void setUpGraphics(GraphicsContext gc) {

        gc.setEffect(this.brush.getEffect().lineEffect);
        gc.setStroke(this.brush.getCurrentColor().getColor());
        gc.setLineWidth(this.brush.getCurrentWidth());
    }

    //<editor-fold desc = "Equality">
    public boolean equals(CanvasLine line) {
        return line != null && line.points.equals(this.points) && this.brush.equals(line.brush);
    }

    // @@@@ "Overriding and benefiting from equals and hashcode."
    @Override
    public boolean equals(Object other) {
        boolean equal = false;
        if (other instanceof  CanvasLine) {
            equal = equals((CanvasLine) other);
        }
        return equal;
    }

    @Override
    public int hashCode() {
        return this.points.hashCode() ^ this.brush.hashCode();
    }
//</editor-fold>
    @Override
    public String toString() {
        return String.format("%s: %s", this.brush, this.points);
    }
}
