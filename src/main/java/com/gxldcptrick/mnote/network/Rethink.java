package com.gxldcptrick.mnote.network;

import com.gxldcptrick.mnote.events.CanvasEvents;
import com.gxldcptrick.mnote.events.ClientEvents;
import com.gxldcptrick.mnote.interfaces.StoppableThread;
import com.gxldcptrick.mnote.models.Brush;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.exc.ReqlDriverError;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.Map;

public class Rethink extends Thread{
    private static final RethinkDB r = RethinkDB.r;
    private Connection conn;
    private String linesTable;
    private String clientsTable;
    private String dbName;
    private String host;
    private int port;
    private String lineId;
    private String clientId;
    private boolean connectedToDB;
    private StoppableThread changes;

    public Rethink(String host, String uuid){
        this.clientId = uuid;
        this.linesTable = "lines";
        this.clientsTable = "clients";
        this.host = host;
        this.dbName = "mnote";
        this.port = 28015;
        this.connectedToDB = false;

        try{
            this.conn = r.connection().hostname(host).port(port).db(dbName).connect();
            connectedToDB = true;
            ClientEvents.getInstance().getOnline().onNext(connectedToDB);
        }
        catch(ReqlDriverError e){
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }

    }

    public boolean connectedToRethink(){
        return connectedToDB;
    }

    @Override
    public void run() {
        super.run();
        subscribeToCanvasEvents();
        subscribeToClientEvents();
        updateTableWithClient();
        monitorClientsConnected();
    }

    private void monitorClientsConnected() {
        changes = new StoppableThread() {
            private volatile boolean terminate = false;
            @Override
            public void stop() {
                terminate = true;
            }

            @Override
            public void run() {
                while(!terminate){
                    System.out.println("monitoring clients changes - before");
                    Cursor changeCursor = r.table(clientsTable).changes()
                            .filter(cursor -> cursor.g("new_val")).run(conn);


                    for(Object change : changeCursor){
                        System.out.println(change);
                    }
                }
            }
        };

        changes.run();
    }

    private void subscribeToClientEvents() {
        ClientEvents.getInstance().getDisconnect().subscribe(this::disconnect);
    }

    private void disconnect(WindowEvent windowEvent) {
        close();
    }

    public void close(){
        System.out.println("Disconnecting client");
        changes.stop();
        r.table(clientsTable).filter(r.hashMap("clientId", clientId)).delete().run(conn);
        conn.close();
    }

    private void updateTableWithClient() {
        System.out.println("Send client id to rethinkDB");
        r.table(clientsTable)
            .insert(r.hashMap("clientId", clientId))
                .run(conn);
    }

    @SuppressWarnings("Duplicates")
    private void subscribeToCanvasEvents() {
        CanvasEvents.getInstance().getMouseDownEvents().subscribe(this::startLine);
        CanvasEvents.getInstance().getMouseDragEvents().subscribe(this::writeLine);
        CanvasEvents.getInstance().getMouseUpEvents().subscribe(this::endLine);
    }

    private void startLine(MouseEvent mouseEvent) {
        Map insert = r.table(linesTable)
                .insert(r.hashMap("clientId", clientId)
                        .with("coordinates", r.array(r.hashMap("x", mouseEvent.getX()).with("y", mouseEvent.getY())))
                        .with("brush", Brush.getInstance()))
                .run(conn);
        System.out.println("Sending StartLine to Rethinkdb");

        ArrayList generated_keys = (ArrayList)insert.get("generated_keys");
        lineId = generated_keys.get(0).toString();
    }

    public void writeLine(MouseEvent mouseEvent) {
        System.out.println("Called writeLine");
        drawLine(mouseEvent);
    }

    private void endLine(MouseEvent mouseEvent) {
        System.out.println("Called end line");
        drawLine(mouseEvent);
    }

    private void drawLine(MouseEvent mouseEvent){
        Map status = r.table(linesTable).get(lineId)
                .update(row -> r.hashMap("coordinates", row.g("coordinates")
                        .append(r.hashMap("x", mouseEvent.getX())
                                .with("y", mouseEvent.getY()))))
                .run(conn);

        if ((long)status.get("replaced") == 1L){
            System.out.println("sent and added point to line");
        }
    }
}
