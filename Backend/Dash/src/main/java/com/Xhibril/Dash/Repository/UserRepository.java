package com.Xhibril.Dash.Repository;
import com.Xhibril.Dash.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

   Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.verified = true WHERE u.email = :email")
    void verifyUser(@Param("email") String email);


    @Modifying
    @Query("UPDATE User u SET u.pass = :pass WHERE u.email = :email")
    void updatePassword(@Param("pass") String pass,
                        @Param("email") String email);
}
