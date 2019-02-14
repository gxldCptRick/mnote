package com.gxldcptrick.mnote.network;

import com.gxldcptrick.mnote.network.data.model.DrawingPackage;
import com.gxldcptrick.mnote.network.data.model.LinePackage;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Insert;
import com.rethinkdb.net.Connection;

import java.util.ArrayList;
import java.util.Map;

public class RethinkAPI {
    private static final RethinkDB r = RethinkDB.r;
    private Connection conn;
    private String tableName;
    private String dbName;
    private String host;
    private int port;

    public RethinkAPI() {
        tableName = "lines";
        host = "localhost";
        dbName = "mnote";
        port = 28015;
        conn = r.connection().hostname(host).port(port).db(dbName).connect();
    }

    public String startLine(DrawingPackage drawingPackage) {
        Map insert = r.table(tableName)
                .insert(r.hashMap("coordinates", r.array(r.hashMap("x", drawingPackage.getPoint2d().getX()).with("y", drawingPackage.getPoint2d().getY())))
                        .with("brush", drawingPackage.getBrush())
                        .with("pointType", drawingPackage.getType()))
                .run(conn);
        ArrayList generated_keys = (ArrayList)insert.get("generated_keys");

        return generated_keys.get(0).toString();
    }
    public void writeLine(LinePackage linePackage) {
        System.out.println("Called writeLine");
        Map status = r.table(tableName).get(linePackage.getLineID())
                .update(row -> r.hashMap("coordinates", row.g("coordinates")
                        .append(r.hashMap("x", linePackage.getX())
                                .with("y", linePackage.getY()))))
                .run(conn);

        if ((long)status.get("replaced") == 1L){
            System.out.println("sent and added point to line");
        }
    }
}
