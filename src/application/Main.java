package application;

import java.awt.Toolkit;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	private Group mainGroup;
	private DrawableCanvas drawSurface;
	
	@Override
	public void start(Stage primaryStage) {
		
		drawSurface = new DrawableCanvas(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		
		mainGroup = new Group();

		mainGroup.getChildren().addAll(drawSurface);
		
		drawSurface.prefWidthProperty().bind(primaryStage.widthProperty().multiply(.99));
		drawSurface.prefHeightProperty().bind(primaryStage.heightProperty().multiply(1.03));

		Scene mainScene = new Scene(mainGroup, 1750, 500);

		primaryStage.setScene(mainScene);


		primaryStage.setTitle("MNote");

		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
