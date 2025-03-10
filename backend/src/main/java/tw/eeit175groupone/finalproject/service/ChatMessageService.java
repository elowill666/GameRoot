package tw.eeit175groupone.finalproject.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tw.eeit175groupone.finalproject.dao.ChatMessageRepository;
import tw.eeit175groupone.finalproject.domain.ChatMessages;
import tw.eeit175groupone.finalproject.exception.ChatRoomNotFoundException;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;

    // 存訊息的方法
    @Transactional
    public ChatMessages save(ChatMessages chatMessages) {
        var chatId = chatRoomService
                .getChatRoomId(chatMessages.getSenderId(), chatMessages.getRecipientId(), true)
                .orElseThrow(() -> new ChatRoomNotFoundException("無法創建或找到聊天室"));

        chatMessages.setChatId(chatId);
        return repository.save(chatMessages);
    }

    // 利用senderId recipientId 取得對話紀錄（增加分頁功能）
    public List<ChatMessages> findChatMessages(String senderId, String recipientId) {
        return chatRoomService.getChatRoomId(senderId, recipientId, false)
                .map(repository::findByChatId)
                .orElse(new ArrayList<>());
    }

    // 新增：獲取特定數量的最新訊息
    public List<ChatMessages> findRecentMessages(String senderId, String recipientId, int limit) {
        return chatRoomService.getChatRoomId(senderId, recipientId, false)
                .map(chatId -> repository.findTopByChatIdOrderByTimestampDesc(chatId))
                .orElse(new ArrayList<>());
    }

}
