package com.example.musicroyalties.services;

import com.example.musicroyalties.models.PassportPhoto;
import com.example.musicroyalties.models.User;
import com.example.musicroyalties.repositories.PassportPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
public class PassportPhotoService {
    
    @Autowired
    private PassportPhotoRepository passportPhotoRepository;
    
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/images/";
    
    public PassportPhoto postPhoto(MultipartFile file, String imageTitle, User user) throws IOException {
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }
        
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        
        String originalFileName = file.getOriginalFilename();
        String fileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'))
                + "_" + System.currentTimeMillis()
                + originalFileName.substring(originalFileName.lastIndexOf('.'));
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        
        String fileUrl = "https://api.owellserver.ggff.net/api/passportphoto/view/" + fileName;
        PassportPhoto photo = new PassportPhoto();
        photo.setFileType(file.getContentType());
        photo.setImageUrl(fileUrl);
        photo.setImageTitle(imageTitle);
        photo.setDatePosted(LocalDate.now());
        photo.setUser(user);
        return passportPhotoRepository.save(photo);
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
    
    public List<PassportPhoto> getAll() {
        return passportPhotoRepository.findAll();
    }
    
    public Optional<PassportPhoto> getById(Long id) {
        return passportPhotoRepository.findById(id);
    }
    
    public Optional<PassportPhoto> getByUserId(Long userId) {
        return passportPhotoRepository.findByUserId(userId);
    }
    
    public void delete(Long id) {
        passportPhotoRepository.deleteById(id);
    }
    
    public PassportPhoto updatePhoto(Long id, MultipartFile file, String imageTitle) throws IOException {
        PassportPhoto photo = passportPhotoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found"));
        
        if (file != null && !file.isEmpty()) {
            if (!file.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are allowed");
            }
            
            String originalFileName = file.getOriginalFilename();
            String fileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'))
                    + "_" + System.currentTimeMillis()
                    + originalFileName.substring(originalFileName.lastIndexOf('.'));
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            
            String fileUrl = "https://api.owellserver.ggff.net/api/passportphoto/view/" + fileName;
            photo.setFileType(file.getContentType());
            photo.setImageUrl(fileUrl);
        }
        photo.setImageTitle(imageTitle);
        photo.setDatePosted(LocalDate.now());
        return passportPhotoRepository.save(photo);
    }
}
