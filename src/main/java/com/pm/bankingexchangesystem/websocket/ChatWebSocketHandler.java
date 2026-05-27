package com.pm.bankingexchangesystem.websocket;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {


    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("📥 [WebSocket] Yeni şəbəkə bağlantısı quruldu. Sessiya ID: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("[WebSocket] Xam məlumat gəldi: {}", payload);

        try {
            ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);

            chatMessage.setContent(chatMessage.getContent() + " (Verified Session: " + session.getId() + ")");

            String broadcastPayload = objectMapper.writeValueAsString(chatMessage);
            TextMessage broadcastMessage = new TextMessage(broadcastPayload);

            broadcast(broadcastMessage);

        } catch (IOException e) {
            log.error("[WebSocket] JSON parse xətası baş verdi. Düzgün format deyil!", e);
            session.sendMessage(new TextMessage("{\"error\": \"Invalid JSON format. Expected keys: sender, content, type\"}"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.warn("out [WebSocket] Bağlantı qapandı. Sessiya ID: {}, Status: {}", session.getId(), status);
    }

    private void broadcast(TextMessage message) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(message);
                } catch (IOException e) {
                    log.error(" [WebSocket] Mesaj göndərilərkən xəta (Sessiya: {})", session.getId(), e);
                }
            }
        }
    }
}
