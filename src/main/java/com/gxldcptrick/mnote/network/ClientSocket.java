package com.gxldcptrick.mnote.network;

import com.gxldcptrick.mnote.FXView.models.DrawingPackage;

import java.io.*;
import java.net.Socket;

public class ClientSocket extends Thread {
    private DrawingPackage drawingPackageSent;
    private DrawingPackage drawingPackageRead;

    static private Socket socket;
    static private ObjectOutputStream out;
    static private ObjectInputStream serverInput;
    private boolean connected;

    public ClientSocket() {
        connected = true;
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
            try {
                while ((drawingPackageRead = DrawingPackage.class.cast(serverInput.readObject())) != null) {
                    System.out.println("drawing pack is not null : " + drawingPackageRead != null);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        };
        try {
            socket = new Socket("localhost", 4444);
            serverInput = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            Thread r = new Thread(read);
            r.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPackageToServer(DrawingPackage drawingPackage) {
        try {
            out.writeObject(drawingPackage);
            WaitToSendPackets();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
