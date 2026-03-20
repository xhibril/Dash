package com.Xhibril.Dash.repository;
import com.Xhibril.Dash.model.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {

    Optional<PasswordReset> findByEmail(String email);

    void deleteByEmail(String email);

    @Modifying
    @Query("UPDATE PasswordReset u SET u.resetToken = :resetToken, u.tokenExpiresAt = :tokenExpiresAt WHERE u.email = :email")
    void addResetToken(@Param("resetToken")String resetToken,
                       @Param("tokenExpiresAt") Instant tokenExpiresAt,
                       @Param("email") String email);



    @Modifying
    @Query("UPDATE PasswordReset u SET u.code = null WHERE u.email = :email")
    void deleteConfirmationCode(@Param("email") String email);

    @Modifying
    @Query("UPDATE PasswordReset u SET u.attempts = :attempts WHERE u.email = :email")
    void updateRemainingAttempts(@Param("attempts") int attempts,
                                 @Param("email") String email);

}
