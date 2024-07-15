package com.Chub.Controllers;

import com.Chub.DTOs.CatDTO;
import com.Chub.DTOs.OwnerDTO;
import com.Chub.Services.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class OwnerController {
    private final OwnerService _ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService) {
        _ownerService = ownerService;
    }

    @GetMapping("/OwnerController/GetOwnerById/{id}")
    public ResponseEntity<OwnerDTO> GetOwnerById(@PathVariable Long id) {
        OwnerDTO result = _ownerService.FindOwnerByID(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/OwnerController/GetOwnerCats/{id}")
    public ResponseEntity<Set<CatDTO>> GetOwnerCats(@PathVariable Long id) throws Exception {
        Set<CatDTO> result = _ownerService.GetOwnerCats(id);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/OwnerController/CreateOwner")
    public ResponseEntity<OwnerDTO> CreateOwner(@RequestBody OwnerDTO dto) throws Exception {
        OwnerDTO result = _ownerService.AddOwner(dto);
        return ResponseEntity.ok(result);
    }
}
