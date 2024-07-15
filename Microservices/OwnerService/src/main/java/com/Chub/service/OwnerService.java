package com.Chub.service;

import com.Chub.DTOs.CatDTO;
import com.Chub.DTOs.OwnerDTO;
import com.Chub.Entities.Cat;
import com.Chub.dto.mapper.CatMapperDTO;
import com.Chub.dto.mapper.OwnerMapperDTO;
import com.Chub.repositories.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class OwnerService {
    private final OwnerRepository _dao;

    private final KafkaTemplate<String, OwnerDTO> kafkaOwnerTemplate;
    private final KafkaTemplate<String, Set<CatDTO>> kafkaSetTemplate;

    @Autowired
    public OwnerService(OwnerRepository ownerDao, KafkaTemplate<String, OwnerDTO> kafkaOwnerTemplate, KafkaTemplate<String, Set<CatDTO>> kafkaSetTemplate) {
        _dao = ownerDao;
        this.kafkaOwnerTemplate = kafkaOwnerTemplate;
        this.kafkaSetTemplate = kafkaSetTemplate;
    }

    @KafkaListener(topics = "add_owner", groupId = "owner-service")
    public OwnerDTO AddOwner(OwnerDTO owner) throws Exception {
        OwnerMapperDTO ownerMapperDTO = new OwnerMapperDTO();
        if (_dao.findById(owner.getID()).isPresent()) {
            throw new Exception("Owner has already added");
        }
        OwnerDTO result = ownerMapperDTO.MappedToOwnerDTO(_dao.save(ownerMapperDTO.MappedToOwner(owner)));
        kafkaOwnerTemplate.send("added_owner", result);
        return result;
    }

    public void RemoveOwner(OwnerDTO owner) throws Exception {
        OwnerMapperDTO ownerMapperDTO = new OwnerMapperDTO();
        if (_dao.findById(owner.getID()).isEmpty()) {
            throw new Exception("Owner has already removed");
        }
        _dao.delete(ownerMapperDTO.MappedToOwner(owner));
    }

    public void UpdateOwner(OwnerDTO owner) throws Exception {
        OwnerMapperDTO ownerMapperDTO = new OwnerMapperDTO();
        if (_dao.findById(owner.getID()).isEmpty()) {
            throw new Exception("Owner doesn't exist");
        }
        _dao.save(ownerMapperDTO.MappedToOwner(owner));
    }

    @KafkaListener(topics = "find_owner", groupId = "owner-service")
    public OwnerDTO FindOwnerByID(Long id) {
        OwnerMapperDTO ownerMapperDTO = new OwnerMapperDTO();
        if (_dao.findById(id).isEmpty()) {
            return null;
        }
        OwnerDTO result = ownerMapperDTO.MappedToOwnerDTO(_dao.findById(id).get());
        kafkaOwnerTemplate.send("found_owner", result);
        return result;
    }

    @KafkaListener(topics = "get_cats", groupId = "owner-service")
    public Set<CatDTO> GetOwnerCats(Long id) throws Exception {
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        if (_dao.findById(id).isEmpty()) {
            throw new Exception("Owner doesn't exist");
        }
        Set<Cat> list = _dao.findById(id).get().GetCatsList();
        Set<CatDTO> dtoList = new HashSet<>();
        for (Cat cat: list) {
            dtoList.add(catMapperDTO.MappedToCatDTO(cat));
        }
        kafkaSetTemplate.send("got_cats", dtoList);

        return dtoList;
    }

    public void BuyCat(CatDTO cat, OwnerDTO owner) throws Exception {
        OwnerMapperDTO ownerMapperDTO = new OwnerMapperDTO();
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        if (_dao.findById(owner.getID()).isEmpty()) {
            throw new Exception("Owner doesn't exist");
        }
        if (_dao.findById(owner.getID()).get().GetCatsList().stream().anyMatch(cat1 -> cat1.getId().equals(cat.getID()))) {
            throw new Exception("This owner has already added this cat");
        }
        ownerMapperDTO.MappedToOwner(owner).AddCat(catMapperDTO.MappedToCat(cat));
        _dao.save(ownerMapperDTO.MappedToOwner(owner));
    }

    public void SellCat(CatDTO cat, OwnerDTO owner) throws Exception {
        OwnerMapperDTO ownerMapperDTO = new OwnerMapperDTO();
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        if (_dao.findById(owner.getID()).isEmpty()) {
            throw new Exception("Owner doesn't exist");
        }
        if (_dao.findById(owner.getID()).get().GetCatsList().stream().noneMatch(cat1 -> cat1.getId().equals(cat.getID()))) {
            throw new Exception("This owner has already removed this cat");
        }
        ownerMapperDTO.MappedToOwner(owner).RemoveCat(catMapperDTO.MappedToCat(cat));
        _dao.save(ownerMapperDTO.MappedToOwner(owner));
    }
}
