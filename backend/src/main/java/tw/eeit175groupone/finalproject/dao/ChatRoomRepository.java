package tw.eeit175groupone.finalproject.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.eeit175groupone.finalproject.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);
}
