package com.example.musicroyalties.services.lookupservices;

import com.example.musicroyalties.models.MemberCategory;
import com.example.musicroyalties.repositories.MemberCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberCategoryRepository mc;

    //post
    public MemberCategory saveMember(MemberCategory members){
        return mc.save(members);
    }
    //get by
    public List<MemberCategory> findByCate(){
        return mc.findAll();
    }
    //get by Id
    public Optional<MemberCategory> findById(Long id){
        return mc.findById(id);
    }

    //Delete
    public void deleteById(Long id){
        mc.deleteById(id);
    }

    //update
    public MemberCategory update(MemberCategory category){
        MemberCategory update = mc.findById(category.getId()).orElse(null);
        update.setCategoryName(category.getCategoryName());
        return mc.save(update);
    }
}
