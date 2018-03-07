package tests.views;

import static org.junit.jupiter.api.Assertions.*;
import static tests.HelperMethods.*;

import java.io.File;

import org.junit.jupiter.api.Test;

import views.CanvasToolbar;

class CanvasToolbarTests {

	@Test
	void canBeSavedAndRestored() {
		
		//arrange
		CanvasToolbar expected = new CanvasToolbar();
		CanvasToolbar actual;
		
		//act
		File save = saveFile("testCT.co", expected);
		
		actual = loadFile(save);
		
		
		//assert
		assertEquals(expected, actual);
		
	}

}
