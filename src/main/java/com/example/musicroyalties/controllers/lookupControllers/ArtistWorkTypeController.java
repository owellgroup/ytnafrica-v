package com.example.musicroyalties.controllers.lookupControllers;

import com.example.musicroyalties.models.ArtistWorkType;
import com.example.musicroyalties.services.lookupservices.ArtistUploadTypeService;
import com.example.musicroyalties.services.lookupservices.ArtistWorkTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/worktype")
public class ArtistWorkTypeController {
    @Autowired
    private ArtistWorkTypeService type;

    //Post maping
    @PostMapping("/post")
    public ArtistWorkType post(@RequestBody ArtistWorkType artistWorkType) {
        return type.postType(artistWorkType);
    }
    //get All
    @GetMapping("/all")
    public List<ArtistWorkType> findAll() {
        return type.getAllWorkTypes();
    }
    //get By Id
    @GetMapping("/{id}")
    public Optional<ArtistWorkType> findById(@PathVariable Long id) {
        return  type.findWorkType(id);
    }

    //Delete
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        type.deleteById(id);
    }

    //update
    @PutMapping("/update/{id}")
    public ArtistWorkType updateType (@PathVariable Long id, @RequestBody ArtistWorkType artistWorkType) {
        artistWorkType.setId(id);
        return type.updated(artistWorkType);

    }
}
