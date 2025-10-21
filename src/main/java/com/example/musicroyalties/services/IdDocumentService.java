package com.example.musicroyalties.services;

import com.example.musicroyalties.models.IdDocument;
import com.example.musicroyalties.models.User;
import com.example.musicroyalties.repositories.IdDocumentRepository;
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
public class IdDocumentService {
    
    @Autowired
    private IdDocumentRepository idDocumentRepository;
    
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/documents/";
    
    public IdDocument uploadDocument(MultipartFile file, String documentTitle, User user) throws IOException {
        if (!file.getContentType().equals("application/pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }
        
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        
        String originalFileName = file.getOriginalFilename();
        String fileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'))
                + "_" + System.currentTimeMillis()
                + originalFileName.substring(originalFileName.lastIndexOf('.'));
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        
        String fileUrl = "https://api.owellserver.ggff.net/api/iddocument/view/" + fileName;
        IdDocument document = new IdDocument();
        document.setFileType(file.getContentType());
        document.setFileUrl(fileUrl);
        document.setDocumentTitle(documentTitle);
        document.setDatePosted(LocalDate.now());
        document.setUser(user);
        return idDocumentRepository.save(document);
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
    
    public List<IdDocument> getAll() {
        return idDocumentRepository.findAll();
    }
    
    public Optional<IdDocument> getById(Long id) {
        return idDocumentRepository.findById(id);
    }
    
    public Optional<IdDocument> getByUserId(Long userId) {
        return idDocumentRepository.findByUserId(userId);
    }
    
    public void delete(Long id) {
        idDocumentRepository.deleteById(id);
    }
    
    public IdDocument updateDocument(Long id, MultipartFile file, String documentTitle) throws IOException {
        IdDocument document = idDocumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        
        if (file != null && !file.isEmpty()) {
            if (!file.getContentType().equals("application/pdf")) {
                throw new IllegalArgumentException("Only PDF files are allowed");
            }
            
            String originalFileName = file.getOriginalFilename();
            String fileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'))
                    + "_" + System.currentTimeMillis()
                    + originalFileName.substring(originalFileName.lastIndexOf('.'));
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            
            String fileUrl = "https://api.owellserver.ggff.net/api/iddocument/view/" + fileName;
            document.setFileType(file.getContentType());
            document.setFileUrl(fileUrl);
        }
        document.setDocumentTitle(documentTitle);
        document.setDatePosted(LocalDate.now());
        return idDocumentRepository.save(document);
    }
}
