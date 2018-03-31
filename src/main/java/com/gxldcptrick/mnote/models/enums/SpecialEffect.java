package com.gxldcptrick.mnote.models.enums;

import javafx.scene.effect.*;

///@@@@ "An enum with custom data elements."
public enum SpecialEffect {

    None(null),
    GaussianBlur(new GaussianBlur()),
    BoxBlur(new BoxBlur()),
    Glow(new Glow(.3)),
    MotionBlur(new MotionBlur(-10,20));

    public final Effect lineEffect;

    SpecialEffect(Effect effect) {
        this.lineEffect = effect;
    }

}
