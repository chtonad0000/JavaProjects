package com.Chub.controllers;

import com.Chub.DTOs.CatDTO;
import com.Chub.DTOs.OwnerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
public class OwnerController {
    private final KafkaTemplate<String, Long> kafkaTemplateLong;
    private final KafkaTemplate<String, OwnerDTO> kafkaTemplateDto;
    private CompletableFuture<OwnerDTO> ownerDtoAwait = new CompletableFuture<>();
    private CompletableFuture<OwnerDTO> newOwnerDtoAwait = new CompletableFuture<>();
    private CompletableFuture<Set<CatDTO>> catSetAwait = new CompletableFuture<>();

    public OwnerController(KafkaTemplate<String, Long> kafkaTemplateLong, KafkaTemplate<String, OwnerDTO> kafkaTemplateDto) {
        this.kafkaTemplateLong = kafkaTemplateLong;
        this.kafkaTemplateDto = kafkaTemplateDto;
    }

    @GetMapping("/OwnerController/GetOwnerById/{id}")
    public ResponseEntity<OwnerDTO> GetOwnerById(@PathVariable Long id) {
        try {
            ownerDtoAwait = new CompletableFuture<>();
            kafkaTemplateLong.send("find_owner", id);
            OwnerDTO dto = ownerDtoAwait.get(10, TimeUnit.SECONDS);
            return ResponseEntity.ok(dto);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (TimeoutException e) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        }
    }
    @KafkaListener(topics = "found_owner", groupId = "controller-service")
    public void GetOwnerResponse(OwnerDTO dto) {
        ownerDtoAwait.complete(dto);
    }

    @GetMapping("/OwnerController/GetOwnerCats/{id}")
    public ResponseEntity<Set<CatDTO>> GetOwnerCats(@PathVariable Long id) throws Exception {
        try {
            catSetAwait = new CompletableFuture<>();
            kafkaTemplateLong.send("get_cats", id);
            Set<CatDTO> set = catSetAwait.get(10, TimeUnit.SECONDS);
            return ResponseEntity.ok(set);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (TimeoutException e) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        }
    }
    @KafkaListener(topics = "got_cats", groupId = "controller-service")
    public void GetFriendsResponse(Set<CatDTO> set) {
        catSetAwait.complete(set);
    }

    @PostMapping("/OwnerController/CreateOwner")
    public ResponseEntity<OwnerDTO> CreateOwner(@RequestBody OwnerDTO dto) throws Exception {
        try {
            newOwnerDtoAwait = new CompletableFuture<>();
            kafkaTemplateDto.send("add_owner", dto);
            OwnerDTO ownerDto = newOwnerDtoAwait.get(10, TimeUnit.SECONDS);
            return ResponseEntity.ok(ownerDto);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (TimeoutException e) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        }
    }
    @KafkaListener(topics = "added_owner", groupId = "controller-service")
    public void GetNewOwnerResponse(OwnerDTO dto) {
        newOwnerDtoAwait.complete(dto);
    }

}
