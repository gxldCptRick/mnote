package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import models.enums.SpecialEffect;

public class CanvasLine implements Serializable {

	private static final long serialVersionUID = 7230L;

	private double lineWidth;
	private List<SavablePoint2D> points;
	private SavableColor color;
	private SpecialEffect lineEffect;

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

	public CanvasLine(Color colorOfLine, int lineWidth) {

		this(new SavableColor(colorOfLine), lineWidth, null);

	}

	public void addNextPoint(SavablePoint2D nextPoint) {

		if (nextPoint != null)
			points.add(nextPoint);

	}

	public boolean contains(Point2D point) {

		boolean found = false;

		Iterator<SavablePoint2D> iterator = this.points.iterator();

		while (iterator.hasNext() && !found) {

			Point2D linePoint = iterator.next().get2DPoint();

			found = linePoint.distance(point) < 15;

			System.out.println(linePoint.distance(point));

			System.out.println(found);

		}

		return found;
	}

	public void drawLine(GraphicsContext gc) {
		gc.beginPath();

		gc.setLineWidth(lineWidth);

		gc.setStroke(color.getColor());

		if (this.lineEffect != null) {

			gc.setEffect(lineEffect.lineEffect);

		} else {

			gc.setEffect(null);
		}

		Point2D initialPoint = getInitialPoint().get2DPoint();

		gc.moveTo(initialPoint.getX(), initialPoint.getY());

		Iterator<SavablePoint2D> iterator = points.iterator();

		while (iterator.hasNext()) {

			Point2D nextPoint = iterator.next().get2DPoint();

			if (nextPoint != initialPoint) {

				gc.lineTo(nextPoint.getX(), nextPoint.getY());
				gc.stroke();
			}

		}

		gc.stroke();
		gc.closePath();
	}

	public SavablePoint2D getInitialPoint() {

		return points.get(0);

	}

	public boolean equals(CanvasLine line) {

		return line != null && line.points.equals(this.points) && line.lineWidth == this.lineWidth
				&& line.color.equals(this.color);

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

		return this.points.hashCode() ^ Double.hashCode(this.lineWidth) ^ this.color.hashCode();

	}

	@Override
	public String toString() {

		return points + " " + color + " " + lineWidth;

	}

	public List<SavablePoint2D> getPoints() {

		return new ArrayList<>(this.points);

	}
}
