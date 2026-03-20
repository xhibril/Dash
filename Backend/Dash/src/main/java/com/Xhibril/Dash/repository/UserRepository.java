package com.Xhibril.Dash.repository;
import com.Xhibril.Dash.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

   Optional<User> findByEmail(String email);

   User findEmailById(Long id);

    @Modifying
    @Query("UPDATE User u SET u.verified = true WHERE u.email = :email")
    void verifyUser(@Param("email") String email);


    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    void updatePassword(@Param("password") String password,
                        @Param("email") String email);


    @Modifying
    @Query("UPDATE User u SET u.email  = :email WHERE u.id = :id")
    void updateEmail(@Param("email") String email,
                     @Param("id") Long id);
}
