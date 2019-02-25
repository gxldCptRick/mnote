package com.gxldcptrick.mnote.models;

import java.util.UUID;

public class User {
    private String uuid;

    public User(){
        uuid = UUID.randomUUID().toString();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
