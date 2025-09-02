package com.example.musicroyalties.services.licenseService;

import com.example.musicroyalties.models.license.LegalEntity;
import com.example.musicroyalties.repositories.LegalEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LegalEntityService {
    @Autowired
    private LegalEntityRepository legalEntityRepository;

    //post
    public LegalEntity saveLegal(LegalEntity legalEntity) {
        return legalEntityRepository.save(legalEntity);
    }
    //get all
    public List<LegalEntity> findAll() {
        return legalEntityRepository.findAll();
    }

    //get by Id
    public Optional<LegalEntity> findById(Long id) {
        return legalEntityRepository.findById(id);
    }

    //public delete;
    public void deleteById(Long id) {
        legalEntityRepository.deleteById(id);
    }

}
