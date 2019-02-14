package com.gxldcptrick.mnote.FXView.controllers;

import com.gxldcptrick.mnote.FXView.components.DrawingBoard;
import com.gxldcptrick.mnote.FXView.models.MouseEventArgs;
import com.gxldcptrick.mnote.FXView.models.Notes;
import javafx.scene.Group;

public class NoteController extends DrawingBoardListener{

    private final Notes data;

    public NoteController(final DrawingBoard board){
        super(board);
        this.data = new Notes();
    }

    @Override
    public void attachToBoardEvents(final DrawingBoard board){
        board.noteDoubleClicked.subscribe(this::addNoteToGroup);
    }

    @Override
    public void detachFromBoardEvents(DrawingBoard board) {

    }

    private void addNoteToGroup(Object sender, MouseEventArgs e){
        if(sender instanceof Group){
            var noteGroup = new Group();
            noteGroup.getChildren().add(null);
        }
    }
}
