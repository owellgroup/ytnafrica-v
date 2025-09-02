package com.example.musicroyalties.controllers.licenseController;

import com.example.musicroyalties.models.license.MusicUsageTypes;
import com.example.musicroyalties.services.licenseService.MusicUsageTypeService;
import com.example.musicroyalties.services.lookupservices.ArtistUploadTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usagetypes")
public class MusicUsageController {
    @Autowired
    private MusicUsageTypeService  musicUsageTypeService;

    @PostMapping("/post")
    public MusicUsageTypes createnew (@RequestBody MusicUsageTypes musicUsageTypes) {
        return musicUsageTypeService.save(musicUsageTypes);
    }

    //get all
    @GetMapping("/all")
    public List<MusicUsageTypes> getAll() {
        return musicUsageTypeService.findAll();
    }

    //get  by Id
    @GetMapping("/{if}")
    public Optional<MusicUsageTypes>  byId(@PathVariable int id){
        return  musicUsageTypeService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id){
        musicUsageTypeService.deleteById(id);
    }

    //update
    @PutMapping("/{id}")
    public MusicUsageTypes update(@PathVariable int id, @RequestBody MusicUsageTypes musicUsageTypes) {
        musicUsageTypes.setId(id);
        return  musicUsageTypeService.save(musicUsageTypes);
    }
}
