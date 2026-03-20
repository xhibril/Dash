package com.Xhibril.Dash.repository;
import com.Xhibril.Dash.dto.analytics.ChartDataResponse;
import com.Xhibril.Dash.model.UrlStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UrlStatRepository extends JpaRepository<UrlStat, Long> {

    List<UrlStat> findByBucketBetweenAndUrlId(LocalDateTime start, LocalDateTime end, Long urlId);

    Optional<UrlStat> findByUrlIdAndBucket(Long urlId, LocalDateTime bucket);

    @Modifying
    @Query("UPDATE UrlStat u SET u.visits = :visits WHERE u.urlId = :urlId and u.bucket =:bucket")
    void updateVisits(@Param("visits") Integer visits,
                      @Param("urlId") Long urlId,
                      @Param("bucket") LocalDateTime bucket);


    @Query("""
                  SELECT new com.Xhibril.Dash.dto.analytics.ChartDataResponse(
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


    @Query("""
                  SELECT new com.Xhibril.Dash.dto.analytics.ChartDataResponse(
                                                         HOUR(u.bucket),
                                                         SUM(u.visits)
                                              
                                                            
                                                     )
                                                     FROM UrlStat u
                                                     WHERE u.urlId = :urlId
                                                       AND u.bucket BETWEEN :start AND :end
                                                     GROUP BY HOUR(u.bucket)
                                                     ORDER BY HOUR(u.bucket)
            """)
    List<ChartDataResponse> getDaily(
            Long urlId,
            LocalDateTime start,
            LocalDateTime end
    );



    @Modifying
    @Query("""
DELETE FROM UrlStat s WHERE s.urlId in (
SELECT u.id FROM Url u WHERE u.userId = :userId
)
""")
    void deleteAllByUserUrls(Long userId);

    @Modifying
    void deleteAllByUrlId(Long urlId);
}