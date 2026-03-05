package com.Xhibril.Dash.Service;

import com.Xhibril.Dash.Dto.SupportRequest;
import com.Xhibril.Dash.Model.Support;
import com.Xhibril.Dash.Repository.SupportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;

@Service
public class SupportService {

    @Autowired
    SupportRepository supportRepo;

    public void saveSupportMessage(@RequestBody SupportRequest sq) {
        Support support = new Support();

        support.setEmail(sq.getEmail());
        support.setSubject(sq.getSubject());
        support.setMessage(sq.getMessage());
        support.setCreatedAt(LocalDate.now());
        support.setStatus("OPEN");

        supportRepo.save(support);
    }
}
