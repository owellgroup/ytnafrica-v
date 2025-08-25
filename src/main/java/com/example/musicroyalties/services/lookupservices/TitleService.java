package com.example.musicroyalties.services.lookupservices;

import com.example.musicroyalties.models.Tittle;
import com.example.musicroyalties.repositories.TittleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TitleService {
    @Autowired
    private TittleRepository tittleRepository;

    //post
    public Tittle savePost(Tittle tittle) {
        return tittleRepository.save(tittle);
    }
    //Get all
    public List<Tittle> getTittle(){
        return tittleRepository.findAll();
    }

    //get by Id
    public Optional<Tittle> getTittleById(Long id){
        return tittleRepository.findById(id);
    }

    //Delete
    public void delete(Long id){
        tittleRepository.deleteById(id);
    }

    //update
    public Tittle updatePost(Tittle tittle){
        Tittle up = tittleRepository.findById(tittle.getId()).orElse(null);
        up.setTitleName(tittle.getTitleName());
        return tittleRepository.save(tittle);
    }

}
