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

    public void saveSupportMessage(Long userId, @RequestBody SupportRequest sq) {
        Support support = new Support();

        support.setEmail(sq.getEmail());
        support.setSubject(sq.getSubject());
        support.setMessage(sq.getMessage());
        support.setCreatedAt(LocalDate.now());
        support.setStatus("OPEN");
        support.setUserId(userId);

        supportRepo.save(support);
    }
}
