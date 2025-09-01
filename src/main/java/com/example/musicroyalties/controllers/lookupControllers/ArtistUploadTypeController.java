package com.example.musicroyalties.controllers.lookupControllers;

import com.example.musicroyalties.models.ArtistUploadType;
import com.example.musicroyalties.repositories.ArtistUploadTypeRepository;
import com.example.musicroyalties.services.lookupservices.ArtistUploadTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/uploadtype")
public class ArtistUploadTypeController {
    @Autowired
    private ArtistUploadTypeService art;

    //posting
    @PostMapping("/post")
    public ArtistUploadType postUpload(@RequestBody ArtistUploadType artistUploadType) {
        return art.postType(artistUploadType);

    }

    //get all
    @GetMapping("/all")
    public List<ArtistUploadType> getEverything() {
        return art.getArtistUploadTypes();
    }

    //get by Id
    @GetMapping("/{id}")
    public Optional<ArtistUploadType> getById(@PathVariable Long id) {
        return art.getArtistUploadTypeById(id);
    }

    //Delete
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        art.deleteArtistUploadTypeById(id);
    }

    //update
   @PutMapping("/update/{id}")
    public ArtistUploadType updateType(@PathVariable Long id, @RequestBody ArtistUploadType artistUploadType) {
        artistUploadType.setId(id);
        return art.updateUpload(artistUploadType);
   }


}
