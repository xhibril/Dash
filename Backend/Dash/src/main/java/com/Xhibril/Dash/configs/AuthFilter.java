package com.Xhibril.Dash.configs;
import com.Xhibril.Dash.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;


@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired AuthService authService;

    // check if user id is valid in certain endpoints
    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain
    ) throws IOException, ServletException {

        String path = req.getRequestURI();


        if (!path.startsWith("/api/")) {
            chain.doFilter(req, res);
            return;
        }

        if(path.startsWith("/api/login") ||
                path.startsWith("/api/signup") ||
                path.startsWith("/api/account/status") ||
                path.startsWith("/api/email/resend") ||
                path.startsWith("/api/email/verify") ||
                path.startsWith("/api/password/reset")
        ) {
            chain.doFilter(req, res);
            return;
        }


        Long id = authService.getAuthenticatedId(req);

        if(id == null){
             res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
             return;
        }

        chain.doFilter(req, res);
    }
}
