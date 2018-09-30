package com.gxldcptrick.mnote.network;

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
//        Runnable read = () -> {
//            System.out.println("Read thread started");
//            try {
//                while ((drawingPackageRead = (DrawingPackage) serverInput.readObject()) != null) {
//                    System.out.println("Drawing pack is not null : " + drawingPackageRead != null);
//                }
//            } catch (IOException | ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        };
        try {
            socket = new Socket("localhost", 4444);
            serverInput = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(new DrawingPackage());
            System.out.println("WTF");

//            Thread r = new Thread(read);
//            r.start();
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
