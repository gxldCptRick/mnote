package tests;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class TestApp extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Platform.exit();
	}
	
	public static void main(String[] args) {
		
		TestApp.launch(args);
		
	}

}
