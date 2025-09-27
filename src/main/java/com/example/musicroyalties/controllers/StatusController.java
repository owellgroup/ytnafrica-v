package com.example.musicroyalties.controllers;

import com.example.musicroyalties.models.Status;
import com.example.musicroyalties.services.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/statuses")
public class StatusController {
    @Autowired
    private StatusService statusService;
    @GetMapping
    public List<Status> getAllStatuses() {
        return statusService.getAllStatuses();
    }

    @GetMapping("/{id}")
    public Optional<Status> getStatusById(@PathVariable Long id) {
        return statusService.getStatusById(id);
    }

    @PostMapping
    public Status createStatus(@RequestBody Status status) {
        return statusService.saveStatus(status);
    }

    @DeleteMapping("/{id}")
    public void deleteStatus(@PathVariable Long id) {
        statusService.deleteStatus(id);
    }

    @PutMapping("/{id}")
    public Status updateStatus(@PathVariable Long id, @RequestBody Status status) {
        return statusService.updateStatus(id, status);
    }

}
