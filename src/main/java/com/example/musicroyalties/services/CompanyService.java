package com.example.musicroyalties.services;

import com.example.musicroyalties.models.ArtistWork;
import com.example.musicroyalties.models.Company;
import com.example.musicroyalties.models.LogSheet;
import com.example.musicroyalties.models.User;
import com.example.musicroyalties.repositories.CompanyRepository;
import com.example.musicroyalties.repositories.LogSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private LogSheetRepository logSheetRepository;
    
    @Autowired
    private MusicService musicService;
    
    public Company createCompany(Company company, User user) {
        company.setUser(user);
        return companyRepository.save(company);
    }
    
    public Company getCompanyByUser(User user) {
        return companyRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }
    
    public List<ArtistWork> getApprovedMusic() {
        return musicService.getApprovedMusic();
    }
    
    public LogSheet createLogSheet(String title, User companyUser, List<Long> musicIds) {
        Company company = getCompanyByUser(companyUser);
        
        List<ArtistWork> selectedMusic = musicIds.stream()
                .map(id -> musicService.getMusicById(id)
                        .orElseThrow(() -> new RuntimeException("Music not found with ID: " + id)))
                .toList();
        
        LogSheet logSheet = new LogSheet();
        logSheet.setTitle(title);
        logSheet.setCreatedDate(LocalDate.now());
        logSheet.setCompany(company);
        logSheet.setSelectedMusic(selectedMusic);
        logSheet.setUser(companyUser);
        
        return logSheetRepository.save(logSheet);
    }
    
    public List<LogSheet> getLogSheetsByCompany(User companyUser) {
        Company company = getCompanyByUser(companyUser);
        return logSheetRepository.findByCompany(company);
    }
    //Get All Logsheets
    public List<LogSheet> getAllLogSheets() {
        return logSheetRepository.findAll();
    }
    public Optional<LogSheet> getLogSheetById(Long id) {
        return logSheetRepository.findById(id);
    }
    
//    public LogSheet updateLogSheet(Long id, String title, List<Long> musicIds) {
//        LogSheet logSheet = logSheetRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("LogSheet not found"));
//
//        List<ArtistWork> selectedMusic = musicIds.stream()
//                .map(musicId -> musicService.getMusicById(musicId)
//                        .orElseThrow(() -> new RuntimeException("Music not found with ID: " + musicId)))
//                .toList();
//
//        logSheet.setTitle(title);
//        logSheet.setSelectedMusic(selectedMusic);
//
//        return logSheetRepository.save(logSheet);
//    }

    //Full Fixed Code
    public LogSheet updateLogSheet(Long id, String title, List<Long> musicIds) {
        LogSheet logSheet = logSheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LogSheet not found with id: " + id));

        List<ArtistWork> selectedMusic = musicIds.stream()
                .map(musicId -> musicService.getMusicById(musicId)
                        .orElseThrow(() -> new RuntimeException("Music not found with ID: " + musicId)))
                .collect(Collectors.toList()); // ✅ mutable list

        logSheet.setTitle(title);
        logSheet.setSelectedMusic(selectedMusic);

        return logSheetRepository.save(logSheet);
    }


    //End of Full Fixed Code
//public LogSheet updateLogSheet(Long id, String title, List<Long> musicIds) {
//    LogSheet logSheet = logSheetRepository.findById(id)
//            .orElseThrow(() -> new RuntimeException("LogSheet not found with id: " + id));
//
//    List<ArtistWork> selectedMusic = musicIds.stream()
//            .map(musicId -> musicService.getMusicById(musicId)
//                    .orElseThrow(() -> new RuntimeException("Music not found with ID: " + musicId)))
//            .toList();
//
//    logSheet.setTitle(title);
//    logSheet.setSelectedMusic(selectedMusic);
//
//    return logSheetRepository.save(logSheet);
//}


    public void deleteLogSheet(Long id) {
        logSheetRepository.deleteById(id);
    }
    
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }
    
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }
    
    public Company updateCompany(Long id, Company companyDetails) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        
        existingCompany.setCompanyName(companyDetails.getCompanyName());
        existingCompany.setCompanyAddress(companyDetails.getCompanyAddress());
        existingCompany.setCompanyPhone(companyDetails.getCompanyPhone());
        existingCompany.setCompanyEmail(companyDetails.getCompanyEmail());
        existingCompany.setContactPerson(companyDetails.getContactPerson());
        
        return companyRepository.save(existingCompany);
    }
    
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    public List<LogSheet> getLogSheetByUser(Long userId) {
        return logSheetRepository.findByUserId(userId);
    }

    //
    public List<LogSheet> getLogSheetsByUser(User user) {
        Company company = getCompanyByUser(user);
        return logSheetRepository.findByCompany(company);
    }

}
