package com.pm.bankingexchangesystem.websocket;

import lombok.Data;

@Data
public class ChatMessage {
    private String sender;
    private String content;
    private MessageType type;

    public enum MessageType {
        JOIN, CHAT, LEAVE
    }
}
