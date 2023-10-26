package org.jxiang.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jxiang.chat.ChatMessage;
import org.jxiang.chat.MessageType;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

  private final SimpMessageSendingOperations messagingTemplate;

  @EventListener
  public void handleWebSocketDisconnectListener(final SessionDisconnectEvent event) {
    // broadcast message to all users in the chat room
    final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    final String username = (String) headerAccessor.getSessionAttributes().get("username");
    if (username != null) {
      log.info("User Disconnected : {}", username);

      final ChatMessage chatMessage = ChatMessage.builder().type(MessageType.LEAVE).sender(username).build();
      // Send Message to a topic
      messagingTemplate.convertAndSend("/topic/public", chatMessage);
    }
  }

}
