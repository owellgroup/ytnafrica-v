package com.example.musicroyalties.services.lookupservices;

import com.example.musicroyalties.models.ArtistWorkType;
import com.example.musicroyalties.repositories.ArtistWorkTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class ArtistWorkTypeService {
    @Autowired
    private ArtistWorkTypeRepository artistWorkTypeRepository;

    //saving method
    public ArtistWorkType postType (ArtistWorkType artistWorkType){
        return artistWorkTypeRepository.save(artistWorkType);
    }

    //get all
    public List<ArtistWorkType> getAllWorkTypes(){
        return artistWorkTypeRepository.findAll();
    }
    //get by ID
    public Optional<ArtistWorkType> findWorkType (Long id){
        return artistWorkTypeRepository.findById(id);
    }
    //Delete
    public void deleteById (Long id){
        artistWorkTypeRepository.deleteById(id);
    }
    //update
    public ArtistWorkType updated(ArtistWorkType artistWorkType){
        ArtistWorkType artistWorkType1 = artistWorkTypeRepository.findById(artistWorkType.getId()).orElse(null);
        return artistWorkTypeRepository.save(artistWorkType);
    }
}
