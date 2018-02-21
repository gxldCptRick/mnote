package application;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

	private Canvas mainDrawingCanvas;
	private Group mainGroup;
	private Label header;
	private EventHandler<MouseEvent>[] drawableMouseEvents;
	private double lineWidth = .5;
	private Label currentSize;
	private Color currentColor;

	@Override
	@SuppressWarnings("unchecked")
	public void start(Stage primaryStage) {
		currentColor = Color.BLACK;
		mainGroup = new Group();
	
		Button eraserButton = new Button("Eraser");
		
		drawableMouseEvents = new EventHandler[3];
		
		eraserButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mainDrawingCanvas.getGraphicsContext2D().clearRect(0, 0, mainDrawingCanvas.getWidth(), mainDrawingCanvas.getHeight());
				
			}
			
		});
		
		currentSize = new Label("Current Line Width : " + lineWidth);
		
		
		Button increaseLineSize = new Button("+"), decreaseLineSize = new Button("-");
		
		ColorPicker colorList = new ColorPicker();
			
		increaseLineSize.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				lineWidth+=.1;
				
				updateSize();
				
			}

			
		});
		
		decreaseLineSize.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				lineWidth -=.1;
				updateSize();
			}
			
		});
		
		colorList.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				Object source = event.getSource();
				
				if(ColorPicker.class.isInstance(source)) {

					ColorPicker picker = ColorPicker.class.cast(source);
					
					currentColor =  picker.getValue();
					
					System.out.println(picker.getValue());
				
				}
				
			}
			
			
		});
	
		
		HBox buttonLine = new HBox();
		
		buttonLine.getChildren().addAll(eraserButton,increaseLineSize, decreaseLineSize, currentSize, colorList);
		
		
		setUpMouseEvents();
		
		header = new Label("Hello World");
		
		header.setFont(new Font(300));
		
		mainDrawingCanvas = new Canvas(2000, 500);
		
		
		
		mainDrawingCanvas.widthProperty().bind(primaryStage.widthProperty());
		mainDrawingCanvas.heightProperty().bind(primaryStage.heightProperty());
		
		mainDrawingCanvas.widthProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				
				mainDrawingCanvas.getGraphicsContext2D().restore();
				
			}
			
		});
		
		mainDrawingCanvas.heightProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				
				mainDrawingCanvas.getGraphicsContext2D().restore();
				
			}
			
		});
		
		setUpDrawing();
		mainGroup.getChildren().addAll(header, mainDrawingCanvas, buttonLine);
		
		Scene mainScene = new Scene(mainGroup);

		primaryStage.setScene(mainScene);

		primaryStage.setTitle("MNote");

		primaryStage.show();

	}
	private void updateSize() {
		
		currentSize.setText("Current Line Width: " + lineWidth);
	}
	private void setUpDrawing() {
		
		mainDrawingCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, drawableMouseEvents[0]);

		mainDrawingCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, drawableMouseEvents[1]);

		mainDrawingCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, drawableMouseEvents[2]);

	}

	private void setUpMouseEvents() {
		drawableMouseEvents[0] = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				GraphicsContext gc = mainDrawingCanvas.getGraphicsContext2D();
				gc.lineTo(event.getX(), event.getY());
				System.out.println(event.getZ());
				gc.stroke();
			}

		};

		drawableMouseEvents[1] = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				mainDrawingCanvas.getGraphicsContext2D().closePath();
				mainDrawingCanvas.getGraphicsContext2D().save();
			}

		};

		drawableMouseEvents[2] = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				GraphicsContext gc = mainDrawingCanvas.getGraphicsContext2D();
				gc.beginPath();
				gc.setLineWidth(lineWidth);
				gc.setStroke(currentColor);
				gc.moveTo(event.getX(), event.getY());
			}

		};

	}

	public static void main(String[] args) {
		launch(args);
	}
}
