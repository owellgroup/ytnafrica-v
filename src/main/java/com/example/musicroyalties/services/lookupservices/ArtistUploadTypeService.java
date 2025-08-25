package com.example.musicroyalties.services.lookupservices;

import com.example.musicroyalties.models.ArtistUploadType;
import com.example.musicroyalties.repositories.ArtistUploadTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistUploadTypeService {
    @Autowired
    private ArtistUploadTypeRepository artistUploadTypeRepository;

    //post
    public ArtistUploadType postType (ArtistUploadType artistUploadType){
        return artistUploadTypeRepository.save(artistUploadType);
    }
    //getall
    public List<ArtistUploadType> getArtistUploadTypes(){
        return artistUploadTypeRepository.findAll();
    }

    //get by id
    public Optional<ArtistUploadType> getArtistUploadTypeById(Long id){
        return artistUploadTypeRepository.findById(id);
    }
    //delete
    public void deleteArtistUploadTypeById(Long id){
        artistUploadTypeRepository.deleteById(id);
    }

    //upating the
    public ArtistUploadType updateUpload(ArtistUploadType artistUploadType){
       ArtistUploadType up = artistUploadTypeRepository.findById(artistUploadType.getId()).orElse(null);


        up.setTypeName(artistUploadType.getTypeName());
        return artistUploadTypeRepository.save(up);
    }



}
