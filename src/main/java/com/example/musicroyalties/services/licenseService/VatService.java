package com.example.musicroyalties.services.licenseService;

import com.example.musicroyalties.models.license.VatStatus;
import com.example.musicroyalties.repositories.VatStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VatService {
    @Autowired
    private VatStatusRepository vatStatusRepository;

    //Post
    public VatStatus postnewVatStatus(VatStatus vatStatus) {
        return vatStatusRepository.save(vatStatus);
    }
    //get All
    public List<VatStatus> getAllVatStatus() {
        return vatStatusRepository.findAll();
    }

    //Get by Id
    public Optional<VatStatus> getVatStatus(Long vatStatusId) {
        return vatStatusRepository.findById(vatStatusId);
    }
}
