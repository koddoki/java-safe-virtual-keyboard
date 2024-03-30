package com.top5nacional.virtualkeyboard.dto;

import com.top5nacional.virtualkeyboard.model.Session;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SessionDTO {
    private List<List<Integer>> keys;
    private String sessionToken;

    public SessionDTO(Session session) {
        this.keys = Session.convertKeysToList(session.getKeys());
        this.sessionToken = session.getSessionToken();
    }

    public SessionDTO(List<List<Integer>> keys, String sessionToken) {
        this.keys = keys;
        this.sessionToken = sessionToken;
    }
}
