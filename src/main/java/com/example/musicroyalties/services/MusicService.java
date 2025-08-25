package com.example.musicroyalties.services;

import com.example.musicroyalties.models.ArtistWork;
import com.example.musicroyalties.models.Status;
import com.example.musicroyalties.models.User;
import com.example.musicroyalties.repositories.ArtistWorkRepository;
import com.example.musicroyalties.repositories.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MusicService {
    
    @Autowired
    private ArtistWorkRepository artistWorkRepository;
    
    @Autowired
    private StatusRepository statusRepository;
    
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/music/";
    
    public ArtistWork uploadMusic(MultipartFile file, String title, User user) throws Exception {
        String contentType = file.getContentType();
        
        // Allow only audio and video files
        if (contentType == null || (!contentType.startsWith("audio/") && !contentType.startsWith("video/"))) {
            throw new IllegalArgumentException("Only audio and video files are allowed");
        }
        
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        
        String originalFileName = file.getOriginalFilename();
        String fileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'))
                + "_" + System.currentTimeMillis()
                + originalFileName.substring(originalFileName.lastIndexOf('.'));
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        
        String fileUrl = "http://localhost:8080/api/music/view/" + fileName;
        
        // Set default status to PENDING
        Status pendingStatus = statusRepository.findByStatus(Status.EStatus.PENDING)
                .orElseGet(() -> {
                    Status status = new Status();
                    status.setStatus(Status.EStatus.PENDING);
                    return statusRepository.save(status);
                });
        
        ArtistWork music = new ArtistWork();
        music.setTitleOfWork(title);
        music.setFileUrl(fileUrl);
        music.setFileType(contentType);
        music.setUploadedDate(LocalDate.now());
        music.setUser(user);
        music.setStatus(pendingStatus);
        
        return artistWorkRepository.save(music);
    }
    
    public Resource loadFileAsResource(String fileName) throws MalformedURLException {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(fileName).normalize();
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            if (!filePath.startsWith(uploadPath)) {
                throw new RuntimeException("Invalid file path - attempted directory traversal");
            }
            
            if (!Files.exists(filePath)) {
                throw new RuntimeException("File not found: " + fileName);
            }
            
            if (!Files.isReadable(filePath)) {
                throw new RuntimeException("File is not readable: " + fileName);
            }
            
            Resource resource = new UrlResource(filePath.toUri());
            return resource;
            
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed file URL for: " + fileName + ". Error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load file: " + fileName + ". Error: " + e.getMessage());
        }
    }
    
    public ResponseEntity<Resource> downloadMusic(Long id) throws IOException {
        ArtistWork music = artistWorkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Music not found"));
        
        Resource resource = loadFileAsResource(
            music.getFileUrl().substring(music.getFileUrl().lastIndexOf("/") + 1)
        );
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    public List<ArtistWork> getMusicByUser(User user) {
        return artistWorkRepository.findByUser(user);
    }
    
    public List<ArtistWork> getApprovedMusic() {
        Status approvedStatus = statusRepository.findByStatus(Status.EStatus.APPROVED)
                .orElseThrow(() -> new RuntimeException("Approved status not found"));
        return artistWorkRepository.findByStatusId(approvedStatus.getId());
    }
    
    public Optional<ArtistWork> getMusicById(Long id) {
        return artistWorkRepository.findById(id);
    }
    
    public ArtistWork updateMusic(Long id, MultipartFile file, String title) throws IOException {
        ArtistWork music = artistWorkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Music not found"));
        
        if (file != null && !file.isEmpty()) {
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.startsWith("audio/") && !contentType.startsWith("video/"))) {
                throw new IllegalArgumentException("Only audio and video files are allowed");
            }
            
            String originalFileName = file.getOriginalFilename();
            String fileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'))
                    + "_" + System.currentTimeMillis()
                    + originalFileName.substring(originalFileName.lastIndexOf('.'));
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            
            String fileUrl = "http://localhost:8080/api/music/view/" + fileName;
            music.setFileType(contentType);
            music.setFileUrl(fileUrl);
        }
        
        music.setTitleOfWork(title);
        music.setUploadedDate(LocalDate.now());
        return artistWorkRepository.save(music);
    }
    
    public void deleteMusic(Long id) {
        artistWorkRepository.deleteById(id);
    }
}
