package com.example.musicroyalties.controllers.lookupControllers;

import com.example.musicroyalties.models.MemberCategory;
import com.example.musicroyalties.services.lookupservices.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*")
public class MemberController {
    @Autowired
    private MemberService memberService;

    //Post
    @PostMapping("/post")
    public MemberCategory  createM(@RequestBody MemberCategory memberCategory) {
        return memberService.saveMember(memberCategory);
    }
    //Get All
    @GetMapping("/{id}")
    public Optional<MemberCategory> findById(@PathVariable Long id){
        return memberService.findById(id);
    }
    //Get All
    @GetMapping("/all")
    public List<MemberCategory> findAll(){
        return memberService.findByCate();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteM(@PathVariable Long id){
        memberService.deleteById(id);
    }

    @PutMapping("/update/{id}")
    public MemberCategory updateM(@PathVariable Long id, @RequestBody MemberCategory memberCategory){
        memberCategory.setId(id);
        return memberService.saveMember(memberCategory);
    }
}
