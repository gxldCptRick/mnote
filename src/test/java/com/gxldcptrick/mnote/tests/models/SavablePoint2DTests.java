package com.gxldcptrick.mnote.tests.models;

import org.junit.Test;

import com.gxldcptrick.mnote.models.SavablePoint2D;

import javafx.geometry.Point2D;

import static com.gxldcptrick.mnote.tests.IOMethods.*;
import static org.junit.Assert.*;

import java.io.File;

public class SavablePoint2DTests {

	@Test
	public void returnsAPoint2DThatIsNotNull() {
		
		//arrange
		SavablePoint2D point = new SavablePoint2D(10, 10);
		Point2D notExpected = null;
		Point2D actual;
		
		//act
		actual = point.get2DPoint();
		
		//assert
		assertNotEquals(notExpected, actual);
	}
	
	@Test
	public void returnsAPoint2DFromJavaFxLibrary() {
		
		//arrange
		SavablePoint2D point = new SavablePoint2D(10, 10);
		Class<?> expected = Point2D.class;
		Class<?> actual;
		
		//act
		
		actual = point.get2DPoint().getClass();
		//assert
		assertEquals(expected, actual);
	}
	
	@Test
	public void convertsPoint2D() {
		
		//arrange
		Point2D expected = new Point2D(10,10);
		SavablePoint2D testPoint = new SavablePoint2D(expected);
		Point2D actual;
		//act
		actual = testPoint.get2DPoint();
		
		//assert
		assertEquals(expected, actual);
	}
	
	@Test
	public void canBeSavedCorrectly() {
		//arrange
		SavablePoint2D expected = new SavablePoint2D(100,100);
		SavablePoint2D actual;
		//act
		File save = saveFile("pointsave.co", expected);
		actual = loadFile(save);
		
		//assert
		assertNotEquals(actual, null);
		assertEquals(expected.get2DPoint(), actual.get2DPoint());
	}

}
