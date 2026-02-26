package com.Xhibril.Dash.Repository;

import com.Xhibril.Dash.Dto.ChartDataResponse;
import com.Xhibril.Dash.Model.UrlStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UrlStatRepository extends JpaRepository<UrlStat, Long> {


    List<UrlStat> findByUrlId(Long urlId);

    List<UrlStat> findByBucketBetweenAndUrlId(LocalDateTime start, LocalDateTime end, Long urlId);

    Optional<UrlStat> findByUrlIdAndBucket(Long urlId, LocalDateTime bucket);

    @Modifying
    @Query("UPDATE UrlStat u SET u.visits = :visits WHERE u.urlId = :urlId and u.bucket =:bucket")
    void updateVisits(@Param("visits") Integer visits,
                      @Param("urlId") Long urlId,
                      @Param("bucket") LocalDateTime bucket);


    @Query("""
                  SELECT new com.Xhibril.Dash.Dto.ChartDataResponse(
                                                         CAST(u.bucket AS LocalDate),
                                                         CAST(SUM(u.visits) AS Long)
                                                     )
                                                     FROM UrlStat u
                                                     WHERE u.urlId = :urlId
                                                       AND u.bucket BETWEEN :start AND :end
                                                     GROUP BY CAST(u.bucket AS LocalDate)
                                                     ORDER BY CAST(u.bucket AS LocalDate)
            """)
    List<ChartDataResponse> getStats(
            Long urlId,
            LocalDateTime start,
            LocalDateTime end
    );


}