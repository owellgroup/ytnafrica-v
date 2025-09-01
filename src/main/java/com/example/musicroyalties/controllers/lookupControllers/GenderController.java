package com.example.musicroyalties.controllers.lookupControllers;

import com.example.musicroyalties.models.Gender;
import com.example.musicroyalties.services.lookupservices.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/gender")
@RestController
@CrossOrigin(origins = "*")
public class GenderController {
    @Autowired
    private GenderService genderService;

    //Post
    @PostMapping("/post")
    public Gender postGender(@RequestBody Gender gender) {
        return genderService.postgrnder(gender);
    }

    //get all
    @GetMapping("/all")
    public List<Gender> getAllGenders() {
        return genderService.getAll();
    }

    //get by Id
    @GetMapping("/{id}")
    public Optional<Gender> getGender(@PathVariable Long id) {
        return genderService.getGender(id);
    }

    //Deleting
    @DeleteMapping("/delete/{id}")
    public void deleteGender(@PathVariable Long id) {
         genderService.deleteM(id);
    }

    //Update
    @PutMapping("/update/{id}")
    public Gender updateGender(@PathVariable Long id, @RequestBody Gender gender) {
        return genderService.updateGender(gender);
    }

}
