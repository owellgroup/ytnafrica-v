package com.example.musicroyalties.services.licenseService;

import com.example.musicroyalties.models.license.MusicUsageTypes;
import com.example.musicroyalties.repositories.MusicUsageTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MusicUsageTypeService {
    @Autowired
    private MusicUsageTypesRepository musicUsageTypesRepository;

    //post
    public MusicUsageTypes save(MusicUsageTypes musicUsageTypes) {
        return musicUsageTypesRepository.save(musicUsageTypes);
    }

    //get All
    public List<MusicUsageTypes> findAll() {
        return musicUsageTypesRepository.findAll();
    }

    //get by Id
    public Optional<MusicUsageTypes> findById(int id) {
        return musicUsageTypesRepository.findById(id);
    }

    //delete
    public void deleteById(int id) {
        musicUsageTypesRepository.deleteById(id);
    }

    //update
    public MusicUsageTypes updatethis (MusicUsageTypes musicUsageTypes) throws  Exception{
        MusicUsageTypes find = musicUsageTypesRepository.findById(musicUsageTypes.getId()).orElse(null);
        find.setUsageType(musicUsageTypes.getUsageType());
        return  musicUsageTypesRepository.save(find);
    }
}
