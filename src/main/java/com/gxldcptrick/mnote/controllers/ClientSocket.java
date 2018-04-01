package com.gxldcptrick.mnote.controllers;

import com.gxldcptrick.mnote.models.SavablePoint2D;
import javafx.geometry.Point2D;

import java.io.*;
import java.net.Socket;

public class ClientSocket extends Thread {

    private SavablePoint2D savablePoint2D = null;

    @Override
    public void run() {
        try (
                Socket socket = new Socket("127.0.0.1", 4444);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
/*                PrintWriter ps = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));*/
                BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
        ) {

            while (true) {
                if (savablePoint2D != null) {
                    System.out.println("should send somethings");
                    out.writeObject(savablePoint2D);
                    savablePoint2D = null;
                }
                Thread.sleep(125);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(SavablePoint2D point2D) {
        savablePoint2D = point2D;
    }
}
