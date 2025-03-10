package tw.eeit175groupone.finalproject.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tw.eeit175groupone.finalproject.dao.ChatMessageRepository;
import tw.eeit175groupone.finalproject.domain.ChatMessages;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;

    public ChatMessages save(ChatMessages chatMessages) {
        var chatId = chatRoomService
                .getChatRoomId(chatMessages.getSenderId(), chatMessages.getRecipientId(), true)
                .orElseThrow(); // You can create your own dedicated exception
        chatMessages.setChatId(chatId);
        repository.save(chatMessages);
        return chatMessages;
    }

    public List<ChatMessages> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }
}
