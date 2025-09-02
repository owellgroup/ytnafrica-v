package com.example.musicroyalties.services.licenseService;

import com.example.musicroyalties.models.license.NaturalPersonEntity;
import com.example.musicroyalties.repositories.NaturalPersonEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NaturalPersonService {
    @Autowired
    private NaturalPersonEntityRepository naturalPersonEntityRepository;

    //post
    public NaturalPersonEntity  save(NaturalPersonEntity naturalPersonEntity) {
        return naturalPersonEntityRepository.save(naturalPersonEntity);
    }
    //get all
    public List<NaturalPersonEntity> findAll() {
        return naturalPersonEntityRepository.findAll();
    }

    //get by Id
    public Optional<NaturalPersonEntity> findById(Long id) {
        return naturalPersonEntityRepository.findById(id);
    }

    //delete
    public void deletebyId(Long id) {
        naturalPersonEntityRepository.deleteById(id);
    }
}
