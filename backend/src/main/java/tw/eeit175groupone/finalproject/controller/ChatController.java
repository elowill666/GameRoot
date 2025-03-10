package tw.eeit175groupone.finalproject.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.eeit175groupone.finalproject.domain.ChatMessages;
import tw.eeit175groupone.finalproject.domain.ChatNotification;
import tw.eeit175groupone.finalproject.dto.ChatMessage;
import tw.eeit175groupone.finalproject.exception.ChatRoomNotFoundException;
import tw.eeit175groupone.finalproject.service.ChatMessageService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        log.debug("公開聊天室收到訊息: {}", chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessages chatMessages) {
        try {
            // 先存訊息
            ChatMessages savedMsg = chatMessageService.save(chatMessages);
            log.debug("私人訊息已儲存: {}", savedMsg);

            // 存完之後使用messagingTemplate傳給對方
            messagingTemplate.convertAndSendToUser(
                    chatMessages.getRecipientId(),
                    "/queue/messages",
                    new ChatNotification(
                            savedMsg.getId(),
                            savedMsg.getSenderId(),
                            savedMsg.getRecipientId(),
                            savedMsg.getContent()));

        } catch (ChatRoomNotFoundException e) {
            log.error("處理訊息時發生錯誤: {}", e.getMessage());
            // 可以考慮通知發送者訊息傳送失敗
            messagingTemplate.convertAndSendToUser(
                    chatMessages.getSenderId(),
                    "/queue/errors",
                    "訊息傳送失敗：" + e.getMessage());
        }
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessages>> findChatMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId,
            @RequestParam(defaultValue = "50") int limit) {

        try {
            // 獲取最新的訊息
            List<ChatMessages> messages = chatMessageService.findRecentMessages(
                    senderId, recipientId, limit);

            return ResponseEntity.ok(messages);

        } catch (Exception e) {
            log.error("獲取聊天記錄時發生錯誤: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
