package com.gxldcptrick.mnote.commonLib;
@FunctionalInterface
public interface EventListener<TEventArgs extends EventArgs> {
    void callBack(Object sender, TEventArgs args);
}
