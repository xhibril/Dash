package com.Xhibril.Dash.service;
import com.Xhibril.Dash.dto.support.SupportRequest;
import com.Xhibril.Dash.model.Support;
import com.Xhibril.Dash.repository.SupportRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.time.LocalDate;

@Service
public class SupportService {

    private final SupportRepository supportRepo;

    public SupportService(SupportRepository supportRepo){
        this.supportRepo = supportRepo;
    }

    public void saveSupportMessage(Long userId, String email, String subject, String message) {
        Support support = new Support();

        support.setEmail(email);
        support.setSubject(subject);
        support.setMessage(message);
        support.setCreatedAt(LocalDate.now());
        support.setStatus("OPEN");
        support.setUserId(userId);

        supportRepo.save(support);
    }
}
