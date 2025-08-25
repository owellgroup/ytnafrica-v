package com.example.musicroyalties.controllers;

import com.example.musicroyalties.models.*;
import com.example.musicroyalties.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private MemberDetailsService memberDetailsService;
    
    @Autowired
    private CompanyService companyService;
    
    @GetMapping("/pending-profiles")
    public ResponseEntity<?> getPendingProfiles() {
        try {
            return ResponseEntity.ok(memberDetailsService.getPendingMemberDetails());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get pending profiles: " + e.getMessage());
        }
    }
    
    @PostMapping("/profile/approve/{memberId}")
    public ResponseEntity<?> approveProfile(@PathVariable Long memberId, @RequestBody Map<String, String> request) {
        try {
            String ipiNumber = request.get("ipiNumber");
            MemberDetails approvedProfile = memberDetailsService.approveMemberDetails(memberId, ipiNumber);
            return ResponseEntity.ok(approvedProfile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Profile approval failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/profile/reject/{memberId}")
    public ResponseEntity<?> rejectProfile(@PathVariable Long memberId, @RequestBody Map<String, String> request) {
        try {
            String notes = request.get("notes");
            MemberDetails rejectedProfile = memberDetailsService.rejectMemberDetails(memberId, notes);
            return ResponseEntity.ok(rejectedProfile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Profile rejection failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/pending-music")
    public ResponseEntity<?> getPendingMusic() {
        try {
            return ResponseEntity.ok(adminService.getPendingMusic());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get pending music: " + e.getMessage());
        }
    }
    
    @PostMapping("/music/approve/{musicId}")
    public ResponseEntity<?> approveMusic(@PathVariable Long musicId, @RequestBody Map<String, String> request) {
        try {
            String isrcCode = request.get("isrcCode");
            ArtistWork approvedMusic = adminService.approveMusic(musicId, isrcCode);
            return ResponseEntity.ok(approvedMusic);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Music approval failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/music/reject/{musicId}")
    public ResponseEntity<?> rejectMusic(@PathVariable Long musicId, @RequestBody Map<String, String> request) {
        try {
            String notes = request.get("notes");
            ArtistWork rejectedMusic = adminService.rejectMusic(musicId, notes);
            return ResponseEntity.ok(rejectedMusic);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Music rejection failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/company/create")
    public ResponseEntity<?> createCompany(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            String companyName = request.get("companyName");
            String companyAddress = request.get("companyAddress");
            String companyPhone = request.get("companyPhone");
            String contactPerson = request.get("contactPerson");
            
            User user = adminService.createCompanyUser(email, password, companyName, companyAddress, companyPhone, contactPerson);
            
            // Create company details
            Company company = new Company();
            company.setCompanyName(companyName);
            company.setCompanyAddress(companyAddress);
            company.setCompanyPhone(companyPhone);
            company.setCompanyEmail(email);
            company.setContactPerson(contactPerson);
            company.setUser(user);
            
            Company savedCompany = companyService.createCompany(company, user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Company created successfully");
            response.put("company", savedCompany);
            response.put("user", user);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Company creation failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(adminService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get users: " + e.getMessage());
        }
    }
    
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(adminService.getUserById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get user: " + e.getMessage());
        }
    }
    
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = adminService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User update failed: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            adminService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User deletion failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/companies")
    public ResponseEntity<?> getAllCompanies() {
        try {
            return ResponseEntity.ok(companyService.getAllCompanies());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get companies: " + e.getMessage());
        }
    }
}
