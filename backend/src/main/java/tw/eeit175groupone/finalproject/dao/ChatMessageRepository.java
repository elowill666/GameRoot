package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import tw.eeit175groupone.finalproject.domain.ChatMessages;

public interface ChatMessageRepository extends JpaRepository<ChatMessages, Integer> {
    List<ChatMessages> findByChatId(String chatId);

    // 獲取最新的N條消息
    List<ChatMessages> findTopByChatIdOrderByTimestampDesc(String chatId, int limit);

    // 批量更新消息狀態
    @Modifying
    @Query("UPDATE ChatMessages c SET c.status = :status WHERE c.senderId = :senderId AND c.recipientId = :recipientId")
    void updateStatusBySenderIdAndRecipientId(String senderId, String recipientId, String status);
}
