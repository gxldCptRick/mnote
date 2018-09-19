package com.gxldcptrick.mnote.FXView.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gxldcptrick.mnote.FXView.enums.SpecialEffect;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasLine implements Serializable {

    private static final long serialVersionUID = 7230L;

    private double lineWidth;
    private List<SavablePoint2D> points;
    private SavableColor color;
    private SpecialEffect lineEffect;

    //<editor-fold desc = "Constructor">

    public CanvasLine() {
        this(Color.BLACK, .5);
    }

    public CanvasLine(CanvasLine currentLine) {

        this(currentLine.color, currentLine.lineWidth, currentLine.lineEffect);
        this.points = new ArrayList<>(currentLine.points);
    }

    public CanvasLine(SavableColor color, double lineWidth, SpecialEffect effect) {

        this.color = color;
        this.lineWidth = lineWidth;
        this.points = new ArrayList<>();

        if (effect != null)
            this.lineEffect = effect;

    }

    public CanvasLine(Color colorOfLine, double lineWidth, SpecialEffect specialEffect) {

        this(new SavableColor(colorOfLine), lineWidth, specialEffect);

    }

    public CanvasLine(Color colorOfLine, double lineWidth) {

        this(new SavableColor(colorOfLine), lineWidth, null);

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
        } else if (points.size() > 0) {
            SavablePoint2D initialPoint = points.get(0);

            if (initialPoint != null) {
                Point2D dot = initialPoint.get2DPoint();
                gc.beginPath();

                setUpGraphics(gc);

                gc.strokeOval(dot.getX(), dot.getY(), this.lineWidth, this.lineWidth);

                gc.closePath();
            }
        }
    }

    private void setUpGraphics(GraphicsContext gc) {

        if (this.lineEffect != null)
            gc.setEffect(lineEffect.lineEffect);
        else
            gc.setEffect(null);

        gc.setStroke(this.color.getColor());
        gc.setLineWidth(this.lineWidth);
    }

    //<editor-fold desc = "Equality">
    public boolean equals(CanvasLine line) {

        return line != null && line.points.equals(this.points) && line.lineWidth == this.lineWidth
                && line.color.equals(this.color) && line.lineEffect == this.lineEffect;

    }

    // @@@@ "Overriding and benefiting from equals and hashcode."

    @Override
    public boolean equals(Object other) {

        boolean equal = false;

        if (getClass().isInstance(other)) {

            equal = equals(getClass().cast(other));

        }

        return equal;

    }

    @Override
    public int hashCode() {

        return this.points.hashCode() ^ Double.hashCode(this.lineWidth) ^ this.color.hashCode() ^ this.lineEffect.hashCode();

    }
//</editor-fold>

    @Override
    public String toString() {

        return points + " " + color + " " + lineWidth;

    }
}
