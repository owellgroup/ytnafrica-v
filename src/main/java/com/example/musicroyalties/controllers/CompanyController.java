package com.example.musicroyalties.controllers;

import com.example.musicroyalties.models.*;
import com.example.musicroyalties.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/company")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('COMPANY')")
public class CompanyController {
    
    @Autowired
    private CompanyService companyService;
    
    @GetMapping("/profile")
    public ResponseEntity<?> getCompanyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;
            Company company = companyService.getCompanyByUser(user);
            return ResponseEntity.ok(company);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get company profile: " + e.getMessage());
        }
    }
    
    @PutMapping("/profile/{id}")
    public ResponseEntity<?> updateCompanyProfile(@PathVariable Long id, @RequestBody Company companyDetails) {
        try {
            Company updatedCompany = companyService.updateCompany(id, companyDetails);
            return ResponseEntity.ok(updatedCompany);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Company profile update failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/approved-music")
    public ResponseEntity<?> getApprovedMusic() {
        try {
            return ResponseEntity.ok(companyService.getApprovedMusic());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get approved music: " + e.getMessage());
        }
    }
    
    @PostMapping("/logsheet")
    public ResponseEntity<?> createLogSheet(@RequestBody Map<String, Object> request,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String title = (String) request.get("title");
            @SuppressWarnings("unchecked")
            List<Long> musicIds = (List<Long>) request.get("musicIds");
            
            User user = (User) userDetails;
            LogSheet logSheet = companyService.createLogSheet(title, user, musicIds);
            return ResponseEntity.ok(logSheet);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("LogSheet creation failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/logsheets")
    public ResponseEntity<?> getLogSheets(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;
            return ResponseEntity.ok(companyService.getLogSheetsByCompany(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get logsheets: " + e.getMessage());
        }
    }
    
    @GetMapping("/logsheet/{id}")
    public ResponseEntity<?> getLogSheetById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(companyService.getLogSheetById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get logsheet: " + e.getMessage());
        }
    }
    
    @PutMapping("/logsheet/{id}")
    public ResponseEntity<?> updateLogSheet(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String title = (String) request.get("title");
            @SuppressWarnings("unchecked")
            List<Long> musicIds = (List<Long>) request.get("musicIds");
            
            LogSheet updatedLogSheet = companyService.updateLogSheet(id, title, musicIds);
            return ResponseEntity.ok(updatedLogSheet);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("LogSheet update failed: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/logsheet/{id}")
    public ResponseEntity<?> deleteLogSheet(@PathVariable Long id) {
        try {
            companyService.deleteLogSheet(id);
            return ResponseEntity.ok("LogSheet deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("LogSheet deletion failed: " + e.getMessage());
        }
    }
    //Get Company By Id
    @GetMapping("/getcompanybyid/{id}")
    public Optional<Company> getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id);
    }
    //Delete Company
    @DeleteMapping("/delete/{id}")
    public void deleteCompanyById(@PathVariable Long id) {
         companyService.deleteCompany(id);
    }
}
