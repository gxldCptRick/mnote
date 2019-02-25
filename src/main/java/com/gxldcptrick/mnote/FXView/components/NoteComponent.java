package com.gxldcptrick.mnote.FXView.components;

import com.gxldcptrick.mnote.FXView.models.NoteData;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.util.Objects;


public class NoteComponent {
    private final NoteData data;
    private Label textDisplay;
    private TextField inputDisplay;
    private Node currentDisplay;
    public Node getCurrentDisplay(){
        return currentDisplay;
    }

    private void SwapDisplay(Group group){
        group.getChildren().remove(currentDisplay);
        if(currentDisplay == inputDisplay){
            currentDisplay = textDisplay;
        }else{
            currentDisplay = textDisplay;
        }
        group.getChildren().add(currentDisplay);
    }

    public NoteComponent(NoteData data, Group group){
        Objects.requireNonNull(data, "data cannot be null");
        textDisplay = new Label();
        inputDisplay = new TextField();
        currentDisplay = inputDisplay;
        this.data = data;
        setLayoutX(data.getxPosition());
        setLayoutY(data.getyPosition());
        setText(data.getContent());
        setupEventsWithGroupWithData(group);
    }

    private void setupEventsWithGroupWithData(Group group) {
        this.inputDisplay.setOnKeyPressed((keyEvent) -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
            }
        });
    }

    public void setText(String text){
        Objects.requireNonNull(text, "text cannot be null.");
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
