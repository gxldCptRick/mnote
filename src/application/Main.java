package application;
	
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		Group og = new Group();
		
		Label helloLabel = new Label("Hello World");
		helloLabel.setFont(new Font(300));
		Canvas canvas = new Canvas();
		
		og.getChildren().addAll(canvas);

		
	
		canvas.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, EventHandler<MouseEvent>(){});
		
	
		
		Scene mainScene = new Scene(og);
	
		
		primaryStage.setScene(mainScene);
		
		primaryStage.setTitle("hello");
		
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
