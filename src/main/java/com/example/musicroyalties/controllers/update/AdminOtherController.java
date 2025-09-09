package com.example.musicroyalties.controllers.update;

import com.example.musicroyalties.models.ArtistWork;
import com.example.musicroyalties.services.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/music2")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOtherController {
    @Autowired
    private MusicService musicService;

    //get All Music
    @GetMapping("getallmusic")
    public List<ArtistWork> getallmusic(){
        return musicService.getAllmusic();
    }

    //get Approved music
    @GetMapping("/getapprovedmusic")
    public List<ArtistWork> getapprovedmusic(){
        return musicService.getApprovedMusic();
    }

    //get music by Users
    @GetMapping("/getmusicbyusersid/{id}")
    public List<ArtistWork> getmusicbyusers(@PathVariable Long id){
        return musicService.getMusicByUserId(id);
    }

    //get Music by Music Id
    @GetMapping("/getmusicbyid/{id}")
    public Optional<ArtistWork> getmusicbyid(@PathVariable Long id){
        return musicService.getMusicById(id);
    }

    //delete Music
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) throws Exception {
        musicService.deleteMusic(id);
    }

   //creating admins

}
