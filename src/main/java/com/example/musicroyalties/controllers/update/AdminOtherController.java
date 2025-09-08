package com.example.musicroyalties.controllers.update;

import com.example.musicroyalties.services.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/music2")
public class AdminOtherController {
    @Autowired
    private MusicService musicService;

    //get All Music

}
