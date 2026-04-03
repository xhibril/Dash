package com.Xhibril.Dash.service;
import com.Xhibril.Dash.dto.support.SupportResponse;
import com.Xhibril.Dash.model.Support;
import com.Xhibril.Dash.repository.SupportRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class SupportService {
    private final SupportRepository supportRepo;

    public SupportService(SupportRepository supportRepo){
        this.supportRepo = supportRepo;
    }

    public ResponseEntity<SupportResponse> saveSupportMessage(Long userId, String email, String subject, String message) {
        Support support = new Support();

        support.setEmail(email);
        support.setSubject(subject);
        support.setMessage(message);
        support.setCreatedAt(LocalDate.now());
        support.setStatus("OPEN");
        support.setUserId(userId);

        supportRepo.save(support);

        return ResponseEntity.ok().body(new SupportResponse("Message sent successfully"));
    }
}
