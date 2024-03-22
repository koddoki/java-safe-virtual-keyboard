package com.top5nacional.virtualkeyboard.dto;

import com.top5nacional.virtualkeyboard.model.Session;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionDTO {
    private String keys;
    private String sessionToken;
    private boolean isActive;

    public SessionDTO(Session session) {
        this.keys = session.getKeys();
        this.sessionToken = session.getSessionToken();
        this.isActive = session.isActive();
    }
}
