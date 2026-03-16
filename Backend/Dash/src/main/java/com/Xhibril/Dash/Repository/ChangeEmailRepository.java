package com.Xhibril.Dash.Repository;


import com.Xhibril.Dash.Model.ChangeEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface ChangeEmailRepository extends JpaRepository<ChangeEmail, Long> {

    Optional<ChangeEmail> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    void deleteAllByUserId(Long id);


    @Modifying
    @Query("""
            UPDATE ChangeEmail u SET u.resetToken = :resetToken, u.resetTokenExpiration = :resetTokenExpiration
            WHERE u.userId = :userId
            """)
    void saveResetToken(@Param("resetToken") String resetToken,
                        @Param("resetTokenExpiration") Instant resetTokenExpiration,
                        @Param("userId") Long userId);



    @Modifying
    @Query("UPDATE ChangeEmail u SET u.verificationCode = null WHERE u.userId = :userId")
    void deleteVerificationCode(@Param("userId") Long userId);
}


