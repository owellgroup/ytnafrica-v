package com.example.musicroyalties.controllers.licenseController;

import com.example.musicroyalties.models.license.NaturalPersonEntity;
import com.example.musicroyalties.services.licenseService.NaturalPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/naturalperson")
public class NaturalPersonController {
    @Autowired
    private NaturalPersonService naturalPersonService;

    //post
    @PostMapping("/post")
    public NaturalPersonEntity createNaturalPerson(@RequestBody NaturalPersonEntity naturalPersonEntity) {
        return naturalPersonService.save(naturalPersonEntity);
    }

    //get all
   @GetMapping("/all")
    public List<NaturalPersonEntity> getAllNaturalPerson() {
        return naturalPersonService.findAll();
   }

   @GetMapping("/{id}")
    public Optional<NaturalPersonEntity> getNaturalPersonById(@PathVariable Long id) {
        return naturalPersonService.findById(id);
   }

   //delete

    @DeleteMapping("/{id}")
    public void deleteNaturalPersonById(@PathVariable Long id) {
        naturalPersonService.deletebyId(id);
    }
}
