package com.gxldcptrick.mnote.FXView.controllers;

import com.gxldcptrick.mnote.FXView.components.DrawingBoard;
import com.gxldcptrick.mnote.FXView.models.MouseEventArgs;
import com.gxldcptrick.mnote.FXView.models.Notes;
import com.gxldcptrick.mnote.commonLib.EventArgs;
import javafx.scene.Group;

public class NoteController extends DrawingBoardListener{

    private final Notes data;
    // maybe later for the ctrl-z
    // private Stack<Notes> oldNotes;

    public NoteController(final DrawingBoard board){
        super(board);
        this.data = new Notes();
    }

    @Override
    public void attachToBoardEvents(final DrawingBoard board){
        board.noteDoubleClicked().subscribe(this::addNoteToGroup);
        board.notesCleared().subscribe(this::clear);
    }

    @Override
    public void detachFromBoardEvents(DrawingBoard board) {
        board.noteDoubleClicked().unsubscribe(this::addNoteToGroup);
        board.notesCleared().unsubscribe(this::clear);
    }

    private void clear(Object sender, EventArgs e){
        if(sender instanceof Group){
            this.data.clear();
        }
    }

    private void addNoteToGroup(Object sender, MouseEventArgs e){
        if(sender instanceof Group){
            var noteGroup = new Group();
            noteGroup.getChildren().add(null);
        }
    }
}
