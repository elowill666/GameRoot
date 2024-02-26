package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.eeit175groupone.finalproject.domain.ChatMessages;

public interface ChatMessageRepository extends JpaRepository<ChatMessages, Integer> {
    List<ChatMessages> findByChatId(String chatId);
}
