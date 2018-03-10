package models.enums;

import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
public enum SpecialEffect {

	GuassianBlur(new GaussianBlur());
	
	public final Effect effect;
	private SpecialEffect(Effect effect){
		this.effect = effect;
	}
	
}
