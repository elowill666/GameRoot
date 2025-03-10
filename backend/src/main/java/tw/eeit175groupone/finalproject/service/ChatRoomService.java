package tw.eeit175groupone.finalproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.eeit175groupone.finalproject.dao.ChatRoomRepository;
import tw.eeit175groupone.finalproject.domain.ChatRoom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    /**
     * 取得或創建聊天室ID
     * 
     * @param senderId          發送者ID
     * @param recipientId       接收者ID
     * @param createIfNotExists 如果不存在是否創建新聊天室
     * @return 聊天室ID的Optional包裝
     */
    public Optional<String> getChatRoomId(
            String senderId,
            String recipientId,
            boolean createIfNotExists) {
        // 進行參數驗證
        if (senderId == null || recipientId == null || senderId.isEmpty() || recipientId.isEmpty()) {
            log.error("發送者ID或接收者ID不能為空");
            return Optional.empty();
        }

        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (createIfNotExists) {
                        log.info("為發送者:{} 和接收者:{} 創建新聊天室", senderId, recipientId);
                        return Optional.of(createChatId(senderId, recipientId));
                    }
                    log.debug("未找到發送者:{} 和接收者:{} 的聊天室", senderId, recipientId);
                    return Optional.empty();
                });
    }

    /**
     * 創建新聊天室並保存雙向關係
     * 
     * @param senderId    發送者ID
     * @param recipientId 接收者ID
     * @return 新創建的聊天室ID
     */
    @Transactional
    private String createChatId(String senderId, String recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        // 建立雙向聊天室關係
        List<ChatRoom> chatRooms = Arrays.asList(
                ChatRoom.builder()
                        .chatId(chatId)
                        .senderId(senderId)
                        .recipientId(recipientId)
                        .build(),
                ChatRoom.builder()
                        .chatId(chatId)
                        .senderId(recipientId)
                        .recipientId(senderId)
                        .build());

        // 批量保存以提高效率
        chatRoomRepository.saveAll(chatRooms);

        return chatId;
    }

}
