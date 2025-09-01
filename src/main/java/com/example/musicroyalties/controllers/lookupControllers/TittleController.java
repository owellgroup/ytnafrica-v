package com.example.musicroyalties.controllers.lookupControllers;

import com.example.musicroyalties.models.Tittle;
import com.example.musicroyalties.services.lookupservices.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tittle")
@CrossOrigin(origins = "*")
public class TittleController {
    @Autowired
    private TitleService titleService;

    //post
    @PostMapping("/post")
    public Tittle saveTittle(@RequestBody Tittle tittle) {
        return titleService.savePost(tittle);
    }
    //get all
    @GetMapping("/all")
    public List<Tittle> findAllTittles() {
        return titleService.getTittle();
    }
    //get by ID
    @GetMapping("/{id}")
    public Optional<Tittle> findTittleById(Long id) {
        return titleService.getTittleById(id);
    }

    //delete
    @DeleteMapping("/delete/{id}")
    public void deleteTittle(@PathVariable Long id) {
         titleService.delete(id);
    }

    //update
    @PutMapping("/update/{id}")
    public Tittle updateTittle(@PathVariable Long id, @RequestBody Tittle tittle) {
        tittle.setId(id);
        return titleService.updatePost(tittle);
    }
}
