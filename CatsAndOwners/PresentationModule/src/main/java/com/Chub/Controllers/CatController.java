package com.Chub.Controllers;

import com.Chub.DTOs.CatBreedDto;
import com.Chub.DTOs.CatColorDto;
import com.Chub.DTOs.CatDTO;
import com.Chub.DTOs.OwnerDTO;
import com.Chub.Services.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class CatController {
    private final CatService _catService;

    @Autowired
    public CatController(CatService catService) {
        _catService = catService;
    }

    @GetMapping("/CatController/FindCatById/{id}")
    public ResponseEntity<CatDTO> GetCatById(@PathVariable Long id) {
        CatDTO result = _catService.FindCatByID(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/CatController/GetCatOwner/{id}")
    public ResponseEntity<OwnerDTO> GetCatOwner(@PathVariable Long id) throws Exception {
        OwnerDTO result = _catService.FindOwner(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/CatController/GetCatFriends/{id}")
    public ResponseEntity<Set<CatDTO>> GetFriendList(@PathVariable Long id) throws Exception {
        Set<CatDTO> result = _catService.GetFriends(id);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/CatController/MakeFriends/{id1},{id2}")
    public void MakeFriends(@PathVariable Long id1, @PathVariable Long id2) throws Exception {
        _catService.MakeFriends(id1, id2);
    }

    @GetMapping("/CatController/GetCats/cats")
    public Set<CatDTO> getCatsByParam(@RequestParam(required = false) CatColorDto color, @RequestParam(required = false)CatBreedDto breed) {
        boolean admin = false;
        Long id = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                if (userDetails.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                    admin = true;
                }
            }
            if (!admin) {
                id = Long.parseLong(authentication.getName());
            }
        }
        if (color != null) {
            return _catService.GetCatsByColor(color, id);
        } else if (breed != null) {
            return _catService.GetCatsByBreed(breed, id);
        }
        return _catService.ShowCats(id);
    }
}
