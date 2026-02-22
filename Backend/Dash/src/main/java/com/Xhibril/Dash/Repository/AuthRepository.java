package com.Xhibril.Dash.Repository;
import com.Xhibril.Dash.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {

   Optional<User> findByEmail(String email);
}
