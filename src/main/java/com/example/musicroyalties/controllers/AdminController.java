package com.example.musicroyalties.controllers;

import com.example.musicroyalties.models.*;
import com.example.musicroyalties.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Autowired
    private PassportPhotoService  passportPhotoService;

    @Autowired
    private UserService userService;

    @Autowired
    private IdDocumentService idDocumentService;

    @Autowired
    private BankConfirmationLetterService bankConfirmationLetterService;

    @Autowired
    private ProofOfPaymentService proofOfPaymentService;
    @Autowired
    private MusicService musicService;
    @Autowired
    private AdminsService adminsService;
    
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
            String companyEmail = request.get("companyEmail");
            
            User user = adminService.createCompanyUser(email, password, companyName, companyAddress, companyPhone, contactPerson, companyEmail);
            
            // Create company details
            Company company = new Company();
            company.setCompanyName(companyName);
            company.setCompanyAddress(companyAddress);
            company.setCompanyPhone(companyPhone);
            company.setCompanyEmail(email);
            company.setContactPerson(contactPerson);
            company.setCompanyEmail(companyEmail);
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

    //update the Company
    @PutMapping("/company/update/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            // Extract fields to update
            String companyName = request.get("companyName");
            String companyAddress = request.get("companyAddress");
            String companyPhone = request.get("companyPhone");
            String contactPerson = request.get("contactPerson");
            String companyEmail = request.get("companyEmail");

            // Build a company object with the new values
            Company companyDetails = new Company();
            companyDetails.setCompanyName(companyName);
            companyDetails.setCompanyAddress(companyAddress);
            companyDetails.setCompanyPhone(companyPhone);
            companyDetails.setContactPerson(contactPerson);
            companyDetails.setCompanyEmail(companyEmail);

            // Call service update
            Company updatedCompany = companyService.updateCompany(id, companyDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Company updated successfully");
            response.put("company", updatedCompany);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Company update failed: " + e.getMessage());
        }
    }

    // Company Update



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
    //Get Profiles
    @GetMapping("/getprofilebyid/{id}")
    public Optional<MemberDetails> getProfileById(@PathVariable Long id) {
        return memberDetailsService.getMemberDetailsById(id);
    }

    @GetMapping("/getallprofiles")
    public List<MemberDetails> getAllProfiles() {
        return memberDetailsService.getAllMemberDetails();
    }



    //get Users Documents By users
    @GetMapping("/userdocumentsandprofiles/{userId}")
    public ResponseEntity<?> getUserDocuments(@PathVariable Long userId) {
        try {
            Map<String, Object> documents = new HashMap<>();
            documents.put("passportphoto", passportPhotoService.getByUserId(userId).orElse(null));
            documents.put("proofOfPayment", proofOfPaymentService.getByUserId(userId).orElse(null));
            documents.put("idDocument", idDocumentService.getByUserId(userId).orElse(null));
            documents.put("bankConfirmationLetter", bankConfirmationLetterService.getByUserId(userId).orElse(null));
            documents.put("memberDetails",memberDetailsService.getByUserId(userId).orElse(null));
            return ResponseEntity.ok(documents);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching documents for userId " + userId + ": " + e.getMessage());
        }
    }

    //get Users Musics by users
    @GetMapping("/usermusic/{userId}")
    public ResponseEntity<?> getMusicByUserId(@PathVariable Long userId) {
        try {
            // Service method should return a list
            List<ArtistWork> musicList = musicService.getMusicByUserId(userId);

            return ResponseEntity.ok(musicList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching music for userId " + userId + ": " + e.getMessage());
        }
    }

    //get all music in the system
    @GetMapping("/getllmusic")
    public List<ArtistWork> getllmusic() {
        return musicService.ListOfMusic();
    }

    //get all companies
    @GetMapping("/getllcompanies")
    public List<Company> getllcompanies() {
        return companyService.getAllCompanies();
    }

    //get Companies by Id
    @GetMapping("/getcompaniesbyid/{id}")
    public Optional<Company> getCompaniesById(@PathVariable Long id) {
        return companyService.getCompanyById(id);
    }

    //delete Company
    @DeleteMapping("/deletecompany/{id}")
    public void deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
    }

    //creating admins
    @PostMapping("/admins/create")
    public ResponseEntity<?> createAdmin(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            String name = request.get("name");
            String role = request.get("role");

            // Create user
            User user = userService.createAdmin(email, password);

            // Create admin details
            Admins admins = new Admins();
            admins.setName(name);
            admins.setRole(role);
            admins.setUser(user);

            Admins savedAdmin = adminsService.post(admins, user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Admin created successfully");
            response.put("admin", savedAdmin);
            response.put("user", user);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Admin creation failed: " + e.getMessage());
        }
    }

    // Update Admin
    @PutMapping("/admins/update/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String name = request.get("name");
            String role = request.get("role");

            Admins admins = new Admins();
            admins.setName(name);
            admins.setRole(role);

            Admins updatedAdmin = adminsService.updateAdminById(admins, id);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Admin updated successfully");
            response.put("admin", updatedAdmin);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Admin update failed: " + e.getMessage());
        }
    }

    // Get all Admins
    @GetMapping("/admins/all")
    public ResponseEntity<?> getAllAdmins() {
        try {
            List<Admins> adminsList = adminsService.getAdmins();
            return ResponseEntity.ok(adminsList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to fetch admins: " + e.getMessage());
        }
    }

    // Get Admin by ID
    @GetMapping("/admins/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable Long id) {
        try {
            return adminsService.getAdminById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to fetch admin: " + e.getMessage());
        }
    }

    // Delete Admin
    @DeleteMapping("/admins/delete/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        try {
            adminsService.deleteAdminById(id);
            return ResponseEntity.ok("Admin deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Admin deletion failed: " + e.getMessage());
        }
    }

    @GetMapping("/getallsheets")
    public List<LogSheet> getAllLogSheets() {
        return companyService.getAllLogSheets();
    }

    @GetMapping("/logsheet/{id}")
    public ResponseEntity<?> getLogSheetById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(companyService.getLogSheetById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get logsheet: " + e.getMessage());
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

    //get Logsheet By Users
    @GetMapping("/logsheetbyuser/{userId}")
    public ResponseEntity<?> getLogSheetByUserId(@PathVariable Long userId) {
        try {
            // Service method should return a list
            List<LogSheet> logsheet = companyService.getLogSheetByUser(userId);

            return ResponseEntity.ok(logsheet);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error " + userId + ": " + e.getMessage());
        }
    }
}


