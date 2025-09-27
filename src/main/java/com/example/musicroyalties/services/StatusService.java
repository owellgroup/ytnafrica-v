package com.example.musicroyalties.services;

import com.example.musicroyalties.models.Status;
import com.example.musicroyalties.repositories.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusService {
    @Autowired
    private StatusRepository statusRepository;

    public List<Status> getAllStatuses() {
        return statusRepository.findAll();
    }

    public Optional<Status> getStatusById(Long id) {
        return statusRepository.findById(id);
    }

    public Status saveStatus(Status status) {
        return statusRepository.save(status);
    }

    public void deleteStatus(Long id) {
        statusRepository.deleteById(id);
    }
    public Status updateStatus(Long id, Status updatedStatus) {
        return statusRepository.findById(id)
                .map(existingStatus -> {
                    existingStatus.setStatus(updatedStatus.getStatus());
                    return statusRepository.save(existingStatus);
                })
                .orElseThrow(() -> new RuntimeException("Status not found with id " + id));
    }


}
