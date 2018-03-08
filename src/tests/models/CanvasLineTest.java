package tests.models;

import static org.junit.jupiter.api.Assertions.*;
import static tests.IOMethods.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;
import models.CanvasLine;
import models.SavablePoint2D;

class CanvasLineTest {

	@Test
	void linesCreatedHavePoints() {
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
	void lineCreatedSaveData() {
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
	void hashCodeIsConsitentWithEquality() {
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
	void hashCodeIsUnique() {
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
	void hashCodeIsConsitent() {
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
	