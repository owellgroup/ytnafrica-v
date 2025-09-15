package com.example.musicroyalties.controllers.licenseController;

import com.example.musicroyalties.models.license.VatStatus;
import com.example.musicroyalties.services.licenseService.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vat")
public class VatController {
    @Autowired
    private VatService vatService;

    //post
    @PostMapping("/post")
    public VatStatus  postVatStatus(@RequestBody VatStatus vatStatus) {
        return vatService.postnewVatStatus(vatStatus);
    }
    //get all
    @GetMapping("/all")
    public List<VatStatus> getAllVatStatus() {
        return vatService.getAllVatStatus();
    }

    //get by Id
    @GetMapping("/{id}")
    public Optional<VatStatus> getVatStatus(@PathVariable Long id) {
        return vatService.getVatStatus(id);
    }
}
