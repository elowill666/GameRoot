package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.eeit175groupone.finalproject.domain.FriendBean;

@Repository
public interface FriendRepository extends JpaRepository<FriendBean, Integer> {
    @Query(value = "SELECT user_detail.user_id,user_detail.username,user_detail.userphoto FROM (SELECT user1_id AS friend_id FROM friend WHERE user2_id = :id UNION SELECT user2_id AS friend_id FROM friend WHERE user1_id = :id) AS unique_friends JOIN user_detail ON unique_friends.friend_id = user_detail.user_id;", nativeQuery = true)
    List<Object[]> getfriendIdandNameAndPhoto(@Param("id") Integer id);

}
