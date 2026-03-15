package com.Xhibril.Dash.Repository;

import com.Xhibril.Dash.Model.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SupportRepository extends JpaRepository<Support, Long> {

    @Modifying
    @Query("DELETE from Support s WHERE s.userId = :userId")
    void deleteAllTickets(Long userId);
}
