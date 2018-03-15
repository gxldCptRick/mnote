package com.gxldcptrick.mnote.tests.models;

import static com.gxldcptrick.mnote.tests.IOMethods.loadFile;
import static com.gxldcptrick.mnote.tests.IOMethods.saveFile;
import static org.junit.Assert.*;
import java.io.File;
import java.util.List;

import org.junit.Test;

import com.gxldcptrick.mnote.models.CanvasLine;
import com.gxldcptrick.mnote.models.SavablePoint2D;

import javafx.scene.paint.Color;

public class CanvasLineTest {

	///@@@@ “At least 3 passing tests using Junit”
	
	@Test
	public void linesCreatedHavePoints() {
		//arrange
		CanvasLine line;
		List<SavablePoint2D> notExpected = null;
		List<SavablePoint2D> actual;
		
		//act
		line = new CanvasLine(Color.BLACK, 0);
		actual = line.getPoints();
		
		//assert
		assertNotEquals(actual, notExpected);
		
	}
	
	@Test
	public void lineCreatedSaveData() {
		//arrange
		CanvasLine expected;
		CanvasLine actual;
		File save;
		
		//act
		expected = new CanvasLine(Color.BLACK, 0);
		save = saveFile("canvasline.co", expected);
		actual = loadFile(save);
		
		//assert
		assertEquals(expected, actual);
		
	}
	
	@Test
	public void hashCodeIsConsitentWithEquality() {
		//arrange
		CanvasLine firstLine = new CanvasLine(Color.BLACK, 1);
		CanvasLine secondLine = new CanvasLine(Color.BLACK, 1);
		int firstHash;
		int secondHash;
		
		//act
		firstHash = firstLine.hashCode();
		secondHash = secondLine.hashCode();
		
		//assert
		assertEquals(firstLine, secondLine);
		assertEquals(firstHash, secondHash);
	}
	
	@Test
	public void hashCodeIsUnique() {
		//arrange
		CanvasLine firstLine = new CanvasLine(Color.BLACK, 1);
		CanvasLine secondLine = new CanvasLine(Color.BLACK, 0);
		int firstHashCode;
		int secondHashCode;
		
		//act
		firstHashCode = firstLine.hashCode();
		secondHashCode = secondLine.hashCode();
		
		//assert
		assertNotEquals(firstHashCode, secondHashCode);
		
	}
	
	@Test
	public void hashCodeIsConsitent() {
		//arrange
		CanvasLine testLine = new CanvasLine(Color.BLACK, 1);
		int expected;
		int actual;
		
		//act
		expected = testLine.hashCode();
		actual = testLine.hashCode();
		
		//asserts
		assertEquals(expected, actual);
		
	}
	
	

}
	