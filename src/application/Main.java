package application;

import java.awt.Toolkit;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	private VBox mainLayout;
	private DrawableCanvas drawSurface;
	private FileMenuToolbar tools;
	
	@Override
	public void start(Stage primaryStage) {
		
		drawSurface = new DrawableCanvas(Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight());

		tools = new FileMenuToolbar();
		
		mainLayout = new VBox();

		mainLayout.getChildren().addAll(tools, drawSurface);
	
		Scene mainScene = new Scene(mainLayout, 1500, 500);

		primaryStage.setScene(mainScene);


		primaryStage.setTitle("MNote");

		drawSurface.prefWidthProperty().bind(primaryStage.widthProperty().multiply(.95));
		drawSurface.prefHeightProperty().bind(primaryStage.heightProperty());

		
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
