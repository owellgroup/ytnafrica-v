package com.example.musicroyalties.services;

import com.example.musicroyalties.models.Admins;
import com.example.musicroyalties.models.User;
import com.example.musicroyalties.repositories.AdminsRepository;
import com.example.musicroyalties.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class AdminsService {
    @Autowired
    private AdminsRepository adminsRepository;

    @Autowired
    private UserRepository userRepository;

    //post
    public Admins post (Admins admins, User user) {
        admins.setUser(user);
        return adminsRepository.save(admins);
    }

    //get all users
    public List<Admins> getAdmins() {
        return adminsRepository.findAll();
    }

    //get users by Id
    public Optional<Admins> getAdminById(Long id) {
        return adminsRepository.findById(id);
    }

    //delete
    public void deleteAdminById(Long id) {
        adminsRepository.deleteById(id);
    }



    //update
    public Admins updateAdminById(Admins admins, Long id) {
        Admins existingAdmin = adminsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        existingAdmin.setRole(admins.getRole());
        existingAdmin.setName(admins.getName());
        return adminsRepository.save(existingAdmin);
    }

}
