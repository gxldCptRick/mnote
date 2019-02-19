package com.gxldcptrick.mnote.FXView.components;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class NoteComponent {
    private Label textDisplay;
    private TextField inputDisplay;
    private Node currentDisplay;
    public Node getCurrentDisplay(){
        return currentDisplay;
    }

    public void SwapDisplay(Group group){
        group.getChildren().remove(currentDisplay);
        if(currentDisplay == inputDisplay){
            currentDisplay = textDisplay;
        }else{
            currentDisplay = textDisplay;
        }
        group.getChildren().add(currentDisplay);
    }

    public NoteComponent(double xPos, double yPos){
        textDisplay = new Label();
        inputDisplay = new TextField();
        currentDisplay = inputDisplay;
        setLayoutX(xPos);
        setLayoutY(yPos);
    }

    public void updateText(String text){
        textDisplay.setText(text);
        inputDisplay.setText(text);
    }

    public void setLayoutX(double xPos){
        textDisplay.setLayoutX(xPos);
        inputDisplay.setLayoutX(xPos);
    }

    public void setLayoutY(double yPos){
        textDisplay.setLayoutY(yPos);
        inputDisplay.setLayoutY(yPos);
    }


}
