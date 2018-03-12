package models.enums;

import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;

///@@@@ "An enum with custom data elements."
public enum SpecialEffect {

	GuassianBlur(new GaussianBlur());
	
	public final Effect lineEffect;
	private SpecialEffect(Effect effect){
		this.lineEffect = effect;
	}
	
}
