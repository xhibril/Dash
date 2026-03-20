package com.Xhibril.Dash.repository;
import com.Xhibril.Dash.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UrlRepository  extends JpaRepository<Url, Long> {

    @Modifying
    void deleteAllByUserId(Long userId);

    boolean existsByShortUrl(String shortUrl);

    Optional<Url> findByShortUrl(String url);


    List<Url> findByUserId(Long userId);

    Url findTopByUserIdOrderByVisitsDesc(Long userId);


    @Modifying
    @Query("UPDATE Url u SET u.visits = :visits WHERE u.shortUrl = :shortUrl")
    void updateVisits(@Param("visits") Integer visits,
                      @Param("shortUrl") String shortUrl);


    boolean existsByIdAndUserId(Long id, Long userId);

    @Modifying
    void deleteUrlById(Long id);
}