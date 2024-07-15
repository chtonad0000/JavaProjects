package com.Chub.controllers;

import com.Chub.DTOs.CatBreedDto;
import com.Chub.DTOs.CatColorDto;
import com.Chub.DTOs.CatDTO;
import com.Chub.DTOs.OwnerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

@RestController
public class CatController {
    private final KafkaTemplate<String, Long> kafkaTemplateLong;

    private final KafkaTemplate<String, List<String>> kafkaTemplateListString;
    private CompletableFuture<CatDTO> catDtoAwait = new CompletableFuture<>();
    private CompletableFuture<OwnerDTO> ownerDtoAwait = new CompletableFuture<>();
    private CompletableFuture<Set<CatDTO>> catSetAwait = new CompletableFuture<>();

    private CompletableFuture<Set<CatDTO>> showCatSetAwait = new CompletableFuture<>();


    @Autowired
    public CatController(KafkaTemplate<String, Long> kafkaTemplateLong, KafkaTemplate<String, List<String>> kafkaTemplateListString) {
        this.kafkaTemplateLong = kafkaTemplateLong;
        this.kafkaTemplateListString = kafkaTemplateListString;
    }

    @GetMapping("/CatController/FindCatById/{id}")
    public ResponseEntity<CatDTO> GetCatById(@PathVariable Long id) throws ExecutionException, InterruptedException, TimeoutException {
        try {
            catDtoAwait = new CompletableFuture<>();
            kafkaTemplateLong.send("find_cat", id);
            CatDTO catDto = catDtoAwait.get(10, TimeUnit.SECONDS);
            return ResponseEntity.ok(catDto);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (TimeoutException e) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        }
    }

    @KafkaListener(topics = "found_cat", groupId = "controller-service")
    public void GetCatResponse(CatDTO dto) {
        catDtoAwait.complete(dto);
    }


    @GetMapping("/CatController/GetCatOwner/{id}")
    public ResponseEntity<OwnerDTO> GetCatOwner(@PathVariable Long id) throws Exception {
        try {
            ownerDtoAwait = new CompletableFuture<>();
            kafkaTemplateLong.send("find_cat_owner", id);
            OwnerDTO ownerDto = ownerDtoAwait.get(10, TimeUnit.SECONDS);
            return ResponseEntity.ok(ownerDto);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (TimeoutException e) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        }
    }

    @KafkaListener(topics = "found_cat_owner", groupId = "controller-service")
    public void GetOwnerResponse(OwnerDTO dto) {
        ownerDtoAwait.complete(dto);
    }

    @GetMapping("/CatController/GetCatFriends/{id}")
    public ResponseEntity<Set<CatDTO>> GetFriendList(@PathVariable Long id) throws Exception {
        try {
            catSetAwait = new CompletableFuture<>();
            kafkaTemplateLong.send("get_friends", id);
            Set<CatDTO> set = catSetAwait.get(10, TimeUnit.SECONDS);
            return ResponseEntity.ok(set);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (TimeoutException e) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        }
    }

    @KafkaListener(topics = "got_friends", groupId = "controller-service")
    public void GetFriendsResponse(Set<CatDTO> set) {
        catSetAwait.complete(set);
    }

    @GetMapping("/CatController/GetCats/cats")
    public ResponseEntity<Set<CatDTO>> getCatsByParam(@RequestParam(required = false) CatColorDto color, @RequestParam(required = false) CatBreedDto breed) {
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

        String topic = "show_cat";
        if (color != null) {
            topic = "color_cat";
        } else if (breed != null) {
            topic = "breed_cat";
        }
        List<String> result = new ArrayList<>();
        if (id != null) {
            result.add(id.toString());
        } else {
            result.add(null);
        }
        if (color != null) {
            result.add(color.toString());
        } else {
            result.add(null);
        }
        if (breed != null) {
            result.add(breed.toString());
        } else {
            result.add(null);
        }
        try {
            showCatSetAwait = new CompletableFuture<>();
            kafkaTemplateListString.send(topic, result);
            Set<CatDTO> set = showCatSetAwait.get(10, TimeUnit.SECONDS);
            return ResponseEntity.ok(set);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (TimeoutException e) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        }
    }

    @KafkaListener(topics = "shown_cat", groupId = "controller-service")
    public void GetShowCatsResponse(Set<CatDTO> set) {
        showCatSetAwait.complete(set);
    }

}
