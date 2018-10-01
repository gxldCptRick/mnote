package com.gxldcptrick.mnote.network;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;

public class ClientSocket extends Thread {
    private DrawingPackage drawingPackageSent;
    private DrawingPackage drawingPackageRead;

    static private Socket socket;
    static private BufferedWriter out;
    static private BufferedReader serverInput;

    private ObjectMapper objectMapper;
    private boolean connected;

    public ClientSocket() {
        connected = true;
        run();
    }

    public void killConnection() {
        this.connected = false;
    }

    private void WaitToSendPackets() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Runnable read = () -> {
            System.out.println("Read thread started");
            String jsonReceived;
            try {
                while ((jsonReceived = serverInput.readLine()) != null) {
                    System.out.println(jsonReceived);

                    drawingPackageRead = objectMapper.readValue(jsonReceived, DrawingPackage.class);
                    System.out.println("Drawing pack is not null : " + drawingPackageRead != null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        try {
            socket = new Socket("localhost", 4444);
            serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            objectMapper = new ObjectMapper();

            Thread r = new Thread(read);
            r.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPackageToServer(DrawingPackage drawingPackage) {
        try {
            out.write(objectMapper.writeValueAsString(drawingPackage));
            System.out.println("packet sent :: sendPackageToServer()");
            WaitToSendPackets();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
