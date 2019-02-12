package com.gxldcptrick.mnote.network;

import com.gxldcptrick.mnote.commonLib.EventArgs;
import com.gxldcptrick.mnote.network.data.model.DrawingPackage;

public class DrawingEventArgs extends EventArgs {
    public final DrawingPackage POINT;
    public DrawingEventArgs(DrawingPackage point){
        POINT = point;
    }
}
