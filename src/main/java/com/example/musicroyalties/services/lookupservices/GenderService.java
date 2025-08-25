package com.example.musicroyalties.services.lookupservices;

import com.example.musicroyalties.models.Gender;
import com.example.musicroyalties.repositories.GenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenderService {

    @Autowired
    private GenderRepository genderRepository;

    //post
    public Gender postgrnder(Gender gender) {
        return genderRepository.save(gender);
    }

    //get All
    public List<Gender> getAll() {
        return genderRepository.findAll();
    }
    //get by id
    public Optional<Gender> getGender(Long id) {
        return genderRepository.findById(id);
    }

    //delete by id
    public void deleteM(Long id) {
        genderRepository.deleteById(id);
    }

    //update
    public Gender updateGender(Gender gender) {
        Gender gen = genderRepository.findById(gender.getId()).orElse(null);
        return genderRepository.save(gender);
    }
}
