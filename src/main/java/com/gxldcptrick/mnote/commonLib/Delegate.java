package com.gxldcptrick.mnote.commonLib;

import java.util.*;

public class Delegate<TArgs extends EventArgs> extends Event<TArgs> {
    public Delegate() {
        super();
    }
    public void invoke(Object sender, TArgs args){
        for (var listener: listeners) {
                listener.callBack(sender, args);
        }
    }
}
