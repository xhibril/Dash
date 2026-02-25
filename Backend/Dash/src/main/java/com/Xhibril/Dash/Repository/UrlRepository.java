package com.Xhibril.Dash.Repository;
import com.Xhibril.Dash.Model.Url;
import com.Xhibril.Dash.Model.UrlStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UrlRepository  extends JpaRepository<Url, Long> {


    boolean existsByShortUrl(String shortUrl);

    Url findByShortUrl(String url);


    List<Url> findByUserId(Long userId);

    List<Url> findByCreatedDateAndId(LocalDate date, Long id);

    Url findTopByUserIdOrderByVisitsDesc(Long userId);


    @Modifying
    @Query("UPDATE Url u SET u.visits = :visits WHERE u.shortUrl = :shortUrl")
    void updateVisits(@Param("visits") Integer visits,
                      @Param("shortUrl") String shortUrl);




}