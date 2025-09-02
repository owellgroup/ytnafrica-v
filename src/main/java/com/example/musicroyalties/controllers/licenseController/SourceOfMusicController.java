package com.example.musicroyalties.controllers.licenseController;

import com.example.musicroyalties.models.license.SourceOfMusic;
import com.example.musicroyalties.services.licenseService.SourceOfMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sourceofmusic")
public class SourceOfMusicController {
    @Autowired
    private SourceOfMusicService sourceOfMusicService;

    //post
    @PostMapping("/post")
    public SourceOfMusic save(@RequestBody SourceOfMusic sourceOfMusic) {
        return sourceOfMusicService.postnew(sourceOfMusic);
    }

    //get All
    @GetMapping("/all")
    public List<SourceOfMusic> getAll() {
        return sourceOfMusicService.findAll();
    }

    //get By id
    @GetMapping("/{id}")
    public Optional<SourceOfMusic> findById(@PathVariable Long id) {
        return sourceOfMusicService.findById(id);
    }

    //delete
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        sourceOfMusicService.deleteById(id);
    }

    //update
    @PutMapping("/{id}")
    public SourceOfMusic update(@PathVariable Long id, @RequestBody SourceOfMusic sourceOfMusic) {
        sourceOfMusic.setId(id);
        return sourceOfMusicService.update(sourceOfMusic);

    }
}
