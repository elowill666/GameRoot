package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.eeit175groupone.finalproject.domain.Status;
import tw.eeit175groupone.finalproject.domain.UserBean;
import tw.eeit175groupone.finalproject.dto.User;

@Repository
public interface UserRepository extends JpaRepository<UserBean, Integer> {

        @Query("SELECT u FROM UserBean u WHERE u.username = :username")
        UserBean findByUsername(@Param("username") String username);

        @Query("SELECT u FROM UserBean u WHERE u.id = :userId")
        UserBean findByUseId(@Param("userId") Integer userId);

        @Query("SELECT u.username FROM UserBean u WHERE u.username = :username")
        UserBean findUsernameByUsername(@Param("username") String username);

        @Modifying
        @Query("UPDATE UserBean u SET u.password = :newpassword WHERE u.username = :username")
        void changePassword(@Param("newpassword") String newpassword, @Param("username") String username);

        @Modifying
        @Query("UPDATE UserBean u SET u.userphoto = :userphoto WHERE u.username = :username")
        Integer updatePhoto(@Param("userphoto") String userphoto, String username);

        @Modifying
        @Query("UPDATE UserBean u SET " +
                        "u.username = :username, " +
                        "u.gender = :gender, " +
                        "u.phone_number = :phone_number, " +
                        "u.email = :email, " +
                        "u.address = :address " +
                        "WHERE u.id = :id")
        void updateProfile(@Param("id") Integer id,
                        @Param("username") String username,
                        @Param("gender") String gender,
                        @Param("phone_number") String phoneNumber,
                        @Param("email") String email,
                        @Param("address") String address);

        List<UserBean> findAllByStatus(Status status);

        
        @Query("SELECT u.id FROM UserBean u ")
        List<Integer> findAllId();
        
        // @Query("SELECT u FROM UserBean u WHERE u.status = :status")
        // List<UserBean> findByStatus(@Param("status") String status);
        @Query("SELECT id,username,email,permissions,bantimecount,bantimestart,bantimeend,cash FROM UserBean")
        List<Object[]> findUserInfo();

        List<UserBean> findAll(Specification<UserBean> spec);
        
}