package com.Xhibril.Dash.configs;

import com.Xhibril.Dash.service.AuthService;
import com.fasterxml.classmate.AnnotationOverrides;
import io.github.bucket4j.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter extends OncePerRequestFilter {



    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private final AuthService authService;

    public RateLimiter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        Long userId = authService.getAuthenticatedId(request);
        String ip = request.getRemoteAddr();
        String path = request.getRequestURI();


        String identifier = null;
        Config con = null;
        for (Config config : configs) {
            if (!config.endpoint.equals("DEFAULT") && path.startsWith(config.endpoint)) {
                con = config;
                break;
            }
        }

        if (con == null) {
            con = configs[configs.length - 1];
        }

        if (con.key.equals("USER") && userId != null) {
            identifier = "USER_" + userId.toString();
        } else {
            identifier = "IP_" + ip;
        }



        boolean allowed = consume(identifier, con, path);
        if (!allowed) {
            response.setStatus(429);
            return;
        }

        filterChain.doFilter(request, response);
    }


    private record Config(
            String endpoint,
            String key,
            int perMin,
            int perHour
    ) {
    }


    private boolean consume(String identifier, Config config, String path) {

        String key = identifier + "_" + (config.endpoint.equals("DEFAULT") ? path : config.endpoint);
        System.out.println("KEY: " + key);

        Bucket bucket = buckets.computeIfAbsent(key, k ->
                Bucket.builder()
                        .addLimit(Bandwidth.simple(config.perMin, Duration.ofMinutes(1)))
                        .addLimit(Bandwidth.simple(config.perHour, Duration.ofHours(1)))
                        .build()
        );
        return bucket.tryConsume(1);
    }


    private static final Config[] configs = {

            // auth
            new Config("/api/signup", "IP", 3, 20),
            new Config("/api/login", "IP", 5, 50),

            // pass reset
            new Config("/api/password-reset/request", "IP", 3, 10),
            new Config("/api/password-reset/verify", "IP", 5, 20),
            new Config("/api/password-reset/reset", "IP", 3, 10),

            // email
            new Config("/api/email/resend", "IP", 2, 10),
            new Config("/api/email/verify", "IP", 5, 30),

            // acc updates
            new Config("/api/update/password", "USER", 3, 10),
            new Config("/api/update-email/request", "USER", 2, 10),
            new Config("/api/update-email/verify", "USER", 5, 20),
            new Config("/api/update-email/change", "USER", 2, 10),

            // logout / delete
            new Config("/api/logout", "USER", 10, 100),
            new Config("/api/delete/account", "USER", 1, 5),


            //support
            new Config("/api/support/tickets", "USER", 5, 30),

            // global
            new Config("DEFAULT", "USER", 100, 1000)

    };


}
