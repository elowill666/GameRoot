package tw.eeit175groupone.finalproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "friend")
public class FriendBean {
    public FriendBean(Integer user1, Integer user2) {
        this.user1_id = user1;
        this.user2_id = user2;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    private Integer id;
    @Column(name = "user1_id")
    private Integer user1_id;
    @Column(name = "user2_id")
    private Integer user2_id;

}
