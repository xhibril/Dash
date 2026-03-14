package com.Xhibril.Dash.Repository;

import com.Xhibril.Dash.Model.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface SupportRepository extends JpaRepository<Support, Long> {
    @Modifying
    void deleteAllByEmail(String email);
}
