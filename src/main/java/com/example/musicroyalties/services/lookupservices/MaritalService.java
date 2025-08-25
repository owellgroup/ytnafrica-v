package com.example.musicroyalties.services.lookupservices;

import com.example.musicroyalties.models.MaritalStatus;
import com.example.musicroyalties.repositories.MaritalStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaritalService {

    @Autowired
    private MaritalStatusRepository marital;

    //post
    public MaritalStatus saveM(MaritalStatus maritalStatus) {
        return marital.save(maritalStatus);
    }

    //Get All
    public List<MaritalStatus> getAllM() {
        return marital.findAll();
    }

    //Get By Id
    public Optional<MaritalStatus> getM(Long id) {
        return marital.findById(id);
    }
    //Delete
    public void deleteM(Long id) {
        marital.deleteById(id);
    }
    //updating it
    public MaritalStatus updateM(MaritalStatus maritalStatus) {
        MaritalStatus ms = marital.findById(maritalStatus.getId()).orElse(null);
        return marital.save(ms);
    }
}
