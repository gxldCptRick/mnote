package application;

import java.awt.Toolkit;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main extends Application {

	private Group mainGroup;
	private Label header;
	private DrawableCanvas drawSurface;
	
	@Override
	public void start(Stage primaryStage) {
		System.out.println("Hello");
		drawSurface = new DrawableCanvas(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		
		mainGroup = new Group();

		mainGroup.getChildren().addAll(drawSurface);

		Scene mainScene = new Scene(mainGroup, 1750, 500);

		primaryStage.setScene(mainScene);

		primaryStage.setTitle("MNote");

		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
