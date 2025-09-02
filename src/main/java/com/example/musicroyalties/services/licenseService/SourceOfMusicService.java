package com.example.musicroyalties.services.licenseService;

import com.example.musicroyalties.models.license.SourceOfMusic;
import com.example.musicroyalties.repositories.SourceOfMusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
public class SourceOfMusicService {
    @Autowired
    private SourceOfMusicRepository sourceOfMusicRepository;

    //post
    public SourceOfMusic postnew (SourceOfMusic sourceOfMusic) {
        return sourceOfMusicRepository.save(sourceOfMusic);
    }
    //get all
    public List<SourceOfMusic> findAll() {
        return sourceOfMusicRepository.findAll();
    }

    //get by Id
    public Optional<SourceOfMusic> findById(Long id) {
        return sourceOfMusicRepository.findById(id);
    }

    //delete
    public void deleteById(Long id) {
        sourceOfMusicRepository.deleteById(id);
    }

    //update
    public SourceOfMusic update (SourceOfMusic sourceOfMusic) {
        SourceOfMusic sourceOfMusic1 = sourceOfMusicRepository.findById(sourceOfMusic.getId()).get();
        sourceOfMusic1.setSourceOfMusic(sourceOfMusic.getSourceOfMusic());
        return sourceOfMusicRepository.save(sourceOfMusic1);
    }
}
