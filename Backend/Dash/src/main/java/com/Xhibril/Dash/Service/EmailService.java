package com.Xhibril.Dash.Service;
import com.Xhibril.Dash.Model.User;

import com.Xhibril.Dash.Repository.UserRepository;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class EmailService {

    @Autowired JwtService jwtService;
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    UserRepository userRepo;

    @Async
    public void sendVerificationEmail(String email) throws Exception {


        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);

        String baseUrl = "http://localhost:8080/api";
        String encodedToken = URLEncoder.encode(jwtService.generateToken("verificationToken", claims, 600), StandardCharsets.UTF_8);
        String link = baseUrl + "/email/verify/" + encodedToken;

        String html = """
        <div style="font-family: Arial; line-height:1.6;">
            <h2>Verify your email</h2>
            <p>Click the button below to verify your account:</p>
            <a href="%s"
               style="
                 display:inline-block;
                 padding:12px 20px;
                 background: #667085;
                 color:white;
                 text-decoration:none;
                 border-radius:6px;
                 font-weight:bold;">
               Verify Email
            </a>
            <p style="margin-top:20px;font-size:12px;color:#666;">
                If you didn’t request this, ignore this email.
            </p>
        </div>
    """.formatted(link);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Verify your email");
        helper.setText(html, true);
        helper.setFrom("noreply@xhibril.dev");
        mailSender.send(message);
    }



    public boolean resendVerificationCode(String email) throws Exception {
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isPresent()) {
            sendVerificationEmail(email);
            return true;
        }
        return false;
    }
}
