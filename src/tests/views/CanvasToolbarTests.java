package tests.views;

import static org.junit.jupiter.api.Assertions.*;
import static tests.HelperMethods.*;

import java.io.File;

import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import tests.TestApp;
import views.CanvasToolbar;

class CanvasToolbarTests {

	@Test
	void canBeSavedAndRestored() {
		
		TestApp.main(null);
		
		Platform.runLater(() -> {
	
			//arrange
			CanvasToolbar expected = new CanvasToolbar();
			CanvasToolbar actual;
			//act
			File save = saveFile("testCT.co", expected);
			
			actual = loadFile(save);
			
			
			//assert
			assertEquals(expected, actual);

			
		});		
	}
	


}
