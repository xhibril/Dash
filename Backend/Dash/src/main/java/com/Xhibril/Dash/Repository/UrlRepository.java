package com.Xhibril.Dash.Repository;
import com.Xhibril.Dash.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository  extends JpaRepository<Url, Long> {


    boolean existsByShortUrl(String shortUrl);

}