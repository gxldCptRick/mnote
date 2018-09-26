package com.gxldcptrick.mnote.tests.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxldcptrick.mnote.FXView.enums.PointType;
import com.gxldcptrick.mnote.FXView.models.Brush;
import com.gxldcptrick.mnote.network.DrawingPackage;
import com.gxldcptrick.mnote.FXView.models.SavablePoint2D;
import com.gxldcptrick.mnote.network.MNoteMultiServer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MNoteServerTests {
    private ObjectMapper mapper;
    private MNoteMultiServer server;

    private void proccessRequest(DrawingPackage... packets) throws IOException {
        try (var connectionToServer = new Socket("127.0.0.1", 4444);
             var writeStream = new PrintStream(connectionToServer.getOutputStream())) {
            for (var packet : packets) {
                var json = mapper.writeValueAsString(packet);
                writeStream.println(json);
                writeStream.flush();
            }
        } catch (IOException e) {
            throw e;
        }
    }

    private void recieveRequest(DrawingPackage... packetsSent) throws Exception {
        DrawingPackage[] packetRecieved = new DrawingPackage[packetsSent.length];
        int count = 0;
        try (var connectionToServer = new Socket("127.0.0.1", 4444);
             var readStream = new BufferedReader(new InputStreamReader(connectionToServer.getInputStream()))) {
            String sentJson;
            while(count < packetRecieved.length && (sentJson = readLine(readStream)) != null){
                 var packet = mapper.readValue(sentJson, DrawingPackage.class);
                 packetRecieved[count++] = packet;
            }
        } catch (Exception e) {
            throw e;
        }
        for(int i = 0; i < packetRecieved.length; i++){
            assertEquals(packetRecieved[i], packetsSent[i]);
        }
    }

    private String readLine(BufferedReader buffy) throws IOException {
        return buffy.readLine();
    }

    @Test
    public void ClientIsAbleToSendASinglePacketToClient() throws InterruptedException, IOException {
        server = new MNoteMultiServer(5);
        var pointSent = generatePointToSend();
        mapper = new ObjectMapper();
        SpawnThreadsForWork(server::start, this.sendPointsToClient(pointSent), this.recievePointFromServer(pointSent));
    }

    private void SpawnThreadsForWork(Runnable... stuffToRun) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(stuffToRun.length);
        for (var stuff : stuffToRun) {
            service.submit(stuff);
        }
        service.awaitTermination(1, TimeUnit.SECONDS);
    }

    private Runnable recievePointFromServer(DrawingPackage... packetsSent) {
        return () -> {
            try {
                this.recieveRequest(packetsSent);
            } catch (Exception e) { }
            finally {
                stopServer(server);
            }
        };
    }

    private Runnable sendPointsToClient(DrawingPackage... packetsToSend) {
        return () ->{
            try {
                this.proccessRequest(packetsToSend);
            } catch (Exception e) { }
        };
    }

    private DrawingPackage generatePointToSend() {
        var pointSent = new DrawingPackage();
        pointSent.setPoint2d(new SavablePoint2D(100, 100));
        pointSent.setType(PointType.MID);
        pointSent.setBrush(new Brush());
        return pointSent;
    }

    private void stopServer(MNoteMultiServer server) {
        server.Stop();
        try {
            new Socket("localhost", 4444);
        } catch (Exception ex) {
        }
    }

    @Test
    public void CanSendMultiplePacketsToServer() throws InterruptedException{
        server = new MNoteMultiServer();
        var pointsToSend = new DrawingPackage[]{ generatePointToSend(), generatePointToSend(), generatePointToSend() , generatePointToSend()};
        mapper = new ObjectMapper();
        SpawnThreadsForWork(server::start, this.sendPointsToClient(pointsToSend), this.recievePointFromServer(pointsToSend));
    }

    @Test
    public void twoUsersCanSendPacketsSimultaneously() throws  InterruptedException{
        server = new MNoteMultiServer();
        var pointsToSend = new DrawingPackage[]{ generatePointToSend(), generatePointToSend(), generatePointToSend() , generatePointToSend()};
        mapper = new ObjectMapper();
        Runnable clientThatSendandRead = () -> {
            var pointsRecieved = new DrawingPackage[pointsToSend.length];
            var count = 0;
            try(var connection = new Socket("127.0.0.1", 4444);
                var readStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                var writeStream = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))){
                for (var point : pointsToSend){
                    var jsonPoint = mapper.writeValueAsString(point);
                    writeStream.write(jsonPoint);
                    writeStream.newLine();
                    writeStream.flush();
                    var recievedJson = readStream.readLine();
                    pointsRecieved[count++] = mapper.readValue(recievedJson, DrawingPackage.class);
                }
                for (int i = 0; i < pointsRecieved.length; i++) {
                        assertEquals(pointsToSend[i], pointsRecieved[i]);
                }
            }catch(IOException e){
            }finally{
                stopServer(server);
            }
        };
        SpawnThreadsForWork(server::start, clientThatSendandRead, clientThatSendandRead);
    }
}
