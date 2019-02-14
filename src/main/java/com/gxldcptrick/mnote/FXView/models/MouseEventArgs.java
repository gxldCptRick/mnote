package com.gxldcptrick.mnote.FXView.models;

import com.gxldcptrick.mnote.commonLib.EventArgs;
import javafx.scene.input.MouseEvent;

public class MouseEventArgs extends EventArgs {
    public final MouseEvent event;
    public MouseEventArgs(MouseEvent event){
        super();
        this.event = event;
    }

}
