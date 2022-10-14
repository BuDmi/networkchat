package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class Message {
    private Timestamp time = null;
    private String nickname;
    private String message;

    public Message(
        @JsonProperty("time") Timestamp time,
        @JsonProperty("nickname") String nickname,
        @JsonProperty("message") String message
    ) {
        this.time = time;
        this.nickname = nickname;
        this.message = message;
    }
}
