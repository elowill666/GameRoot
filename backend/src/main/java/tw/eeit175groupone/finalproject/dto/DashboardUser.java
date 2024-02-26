package tw.eeit175groupone.finalproject.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DashboardUser {
    // 之後再添加訂單資訊之類的資訊
    private Integer id;
    private String username;
    private String password;
private String gender;
private String address;
private String phone_number;
    private String email;
    private String membership;
    private Integer permissions;
    private String userphoto;
    private Date created_at;
    private Integer bantimecount;
    private String banReason;
    private Date bantimestart;
    private Date bantimeend;
    private Integer cash;
    
    
    
    private Integer countUserArticles;
    private Integer countUserComments;
    private Integer confirmedOrder;
    private Integer unconfirmedOrder;
    private Integer totalOrders;
    private Integer totalAmount;

    private Integer start;
    private Integer rows;
    private String sort;
}
