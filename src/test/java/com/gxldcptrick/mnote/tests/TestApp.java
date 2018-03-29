package com.gxldcptrick.mnote.tests;


import org.junit.Test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class TestApp extends Application{

	@Override
	public void start(Stage primaryStage) {
		
		Platform.exit();
	}

	@Test
	public void launchApp(){
		main(null);
	}


	public static void main(String[] args) {
		
		TestApp.launch(args);
		
	}

}
