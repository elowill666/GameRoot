package tw.eeit175groupone.finalproject.domain;

import java.sql.Date;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_detail")
@DynamicUpdate
@Data
public class UserBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "int")
    private Integer id;
    @Column(name = "password", columnDefinition = "varchar")
    private String password;
    @Column(name = "username", columnDefinition = "varchar")
    private String username;
    @Column(name = "gender", columnDefinition = "nvarchar")
    private String gender;
    @Column(name = "email", columnDefinition = "varchar")
    private String email;
    @Column(name = "birth", columnDefinition = "datetime")
    private Date birth;
    @Column(name = "address", columnDefinition = "nvarchar")
    private String address;
    @Column(name = "phone_number", columnDefinition = "varchar")
    private String phone_number;
    @Column(name = "membership", columnDefinition = "varchar")
    private String membership;
    @Column(name = "permissions", columnDefinition = "varchar")
    private Integer permissions;
    @Column(name = "allowchat", columnDefinition = "int")
    private Integer allowchat;
    @Column(name = "userphoto", columnDefinition = "varchar")
    private String userphoto;
    @Column(name = "account", columnDefinition = "varchar")
    private String account;
    @Column(name = "created_at", columnDefinition = "datetime")
    private Date created_at;
    @Column(name = "useraccount_id", columnDefinition = "int")
    private Integer useraccount_id;
    @Column(name = "bantimecount", columnDefinition = "int")
    private Integer bantimecount;
    @Column(name = "bantimestart", columnDefinition = "datetime")
    private Date bantimestart;
    @Column(name = "bantimeend", columnDefinition = "datetime")
    private Date bantimeend;
    @Column(name = "banreason", columnDefinition = "nvarchar")
    private String banReason;
    @Column(name = "cash", columnDefinition = "int")
    private Integer cash;
    @Column(name = "status", columnDefinition = "varchar")
    private String status;
}
