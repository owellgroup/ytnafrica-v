package com.example.musicroyalties.controllers.licenseController;

import com.example.musicroyalties.models.license.LegalEntity;
import com.example.musicroyalties.services.licenseService.LegalEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/legalentity")
public class LegalEntityController {
    @Autowired
    private LegalEntityService legalEntityService;

    //post
    @PostMapping("/post")
    public LegalEntity createLegalEntity(@RequestBody LegalEntity legalEntity) {
        return legalEntityService.saveLegal(legalEntity);
    }

    //get all
    @GetMapping("/all")
    public List<LegalEntity> getAllLegalEntities() {
        return legalEntityService.findAll();
    }
    //get by Id
    @GetMapping("/{id}")
    public Optional<LegalEntity> getLegalEntityById(@PathVariable Long id) {
        return legalEntityService.findById(id);
    }

    //delete
    @DeleteMapping("/{id}")
    public void deleteLegalEntityById(@PathVariable Long id) {
        legalEntityService.deleteById(id);
    }
}
