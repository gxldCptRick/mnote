package com.gxldcptrick.mnote.tests.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxldcptrick.mnote.FXView.models.Brush;
import com.gxldcptrick.mnote.network.data.model.DrawingPackage;
import com.gxldcptrick.mnote.FXView.models.SavablePoint2D;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class DrawingPackageTests {
    @Test
    public void ValidTest() throws IOException {
        var packet = new DrawingPackage();
        packet.setBrush(new Brush());
        packet.setPoint2d(new SavablePoint2D(100,100));
        var mapper = new ObjectMapper();
        var json  = mapper.writeValueAsString(packet);
        System.out.println(json);
        var readPacket = mapper.readValue(json, DrawingPackage.class);
        assertEquals(readPacket, packet);
    }
}
