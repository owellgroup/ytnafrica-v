package com.example.musicroyalties.controllers.lookupControllers;

import com.example.musicroyalties.models.MaritalStatus;
import com.example.musicroyalties.services.lookupservices.MaritalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/martial")
@RestController
@CrossOrigin(origins = "*")
public class MaritalController {
    @Autowired
    private MaritalService  maritalService;

    //Post
    @PostMapping("/post")
    public MaritalStatus createM(@RequestBody MaritalStatus maritalStatus) {
        return maritalService.saveM(maritalStatus);
    }

    //get All
    @GetMapping("/all")
    public List<MaritalStatus> getthemALL() {

        return maritalService.getAllM();
    }

    //get by id
    @GetMapping("/{id}")
    public Optional<MaritalStatus> getM(@PathVariable Long id) {
        return maritalService.getM(id);
    }

    //Delete
    @DeleteMapping("/delete/{id}")
    public void deleteM(@PathVariable Long id) {
        maritalService.deleteM(id);
    }

    //update
    @PutMapping("/update/{id}")
    public MaritalStatus updateM(@RequestBody MaritalStatus maritalStatus, Long id) {
        maritalStatus.setId(id);
        return maritalService.updateM(maritalStatus);
    }
}
