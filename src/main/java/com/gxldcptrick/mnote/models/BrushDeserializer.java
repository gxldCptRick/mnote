package com.gxldcptrick.mnote.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.gxldcptrick.mnote.enums.SpecialEffect;
import javafx.scene.paint.Color;

import java.io.IOException;

public class BrushDeserializer extends JsonDeserializer {
    @Override
    public Brush deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Brush brush = new Brush();

        double currentWidth = node.get("currentWidth").doubleValue();
        SpecialEffect effect = getEffect(node.get("effect"));
        Color color = getColor(node.get("color"));

        brush.setCurrentWidth(currentWidth);
        brush.setEffect(effect);
        brush.setColor(color);

        return brush;
    }

    private Color getColor(JsonNode color) {
        double red = color.get("red").doubleValue();
        double green = color.get("green").doubleValue();
        double blue = color.get("blue").doubleValue();
        double opacity = color.get("opacity").doubleValue();

        return new Color(red, green, blue, opacity);
    }

    private SpecialEffect getEffect(JsonNode effect) {
        SpecialEffect specialEffect;
        switch(effect.toString()){
            case "GaussianBlur":
                specialEffect = SpecialEffect.GaussianBlur;
                break;
            case "BoxBlur":
                specialEffect = SpecialEffect.BoxBlur;
                break;
            case "Glow":
                specialEffect = SpecialEffect.Glow;
                break;
            case "MotionBlur":
                specialEffect = SpecialEffect.MotionBlur;
                break;
            default:
                specialEffect = SpecialEffect.None;
                break;
        }
        return specialEffect;
    }
}
