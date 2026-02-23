package com.Xhibril.Dash.Repository;
import com.Xhibril.Dash.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {

   Optional<User> findByEmail(String email);
}
