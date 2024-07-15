package com.Chub.service;

import com.Chub.DTOs.CatBreedDto;
import com.Chub.DTOs.CatColorDto;
import com.Chub.DTOs.CatDTO;
import com.Chub.DTOs.OwnerDTO;
import com.Chub.Entities.Cat;
import com.Chub.dto.mapper.BreedMapperDTO;
import com.Chub.dto.mapper.CatMapperDTO;
import com.Chub.dto.mapper.ColorMapperDTO;
import com.Chub.dto.mapper.OwnerMapperDTO;
import com.Chub.repositories.CatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class CatService {
    private final CatRepository _dao;

    private final KafkaTemplate<String, CatDTO> kafkaTemplate;

    private final KafkaTemplate<String, OwnerDTO> kafkaOwnerTemplate;
    private final KafkaTemplate<String, Set<CatDTO>> kafkaSetTemplate;

    @Autowired
    public CatService(CatRepository catDao, KafkaTemplate<String, CatDTO> template, KafkaTemplate<String, Set<CatDTO>> setTemplate, KafkaTemplate<String, OwnerDTO> ownerTemplate) {
        _dao = catDao;
        kafkaTemplate = template;
        kafkaSetTemplate = setTemplate;
        kafkaOwnerTemplate = ownerTemplate;
    }

    @KafkaListener(topics = "add_cat", groupId = "cat-service")
    public CatDTO AddCat(CatDTO cat) throws Exception {
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        if (_dao.findById(cat.getID()).isPresent()) {
            throw new Exception("This cat has already added");
        }
        CatDTO result = catMapperDTO.MappedToCatDTO(_dao.save(catMapperDTO.MappedToCat(cat)));
        kafkaTemplate.send("added_cat", result);
        return result;
    }

    @KafkaListener(topics = "remove_cat", groupId = "cat-service")
    public void RemoveCat(CatDTO cat) throws Exception {
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        if (_dao.findById(cat.getID()).isEmpty()) {
            throw new Exception("This cat has already removed");
        }
        _dao.delete(catMapperDTO.MappedToCat(cat));
    }

    @KafkaListener(topics = "update_cat", groupId = "cat-service")
    public void UpdateCat(CatDTO cat) throws Exception {
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        if (_dao.findById(cat.getID()).isEmpty()) {
            throw new Exception("This cat doesn't exist");
        }
        _dao.save(catMapperDTO.MappedToCat(cat));
    }


    @KafkaListener(topics = "find_cat", groupId = "cat-service")
    public CatDTO FindCatByID(Long id) {
        CatDTO result = null;
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        if (! _dao.findById(id).isEmpty()) {
            result = catMapperDTO.MappedToCatDTO(_dao.findById(id).get());
        }
        kafkaTemplate.send("found_cat", result);
        return result;
    }


    public void MakeFriends(Long catId1, Long catId2) throws Exception {
        if (_dao.findById(catId1).isEmpty()) {
            throw new Exception("Cat1 doesnt exist");
        }
        if (_dao.findById(catId2).isEmpty()) {
            throw new Exception("Cat2 doesnt exist");
        }
        Cat cat1 = _dao.findById(catId1).get();
        Cat cat2 = _dao.findById(catId2).get();
        if (GetFriends(catId1).stream().anyMatch(cat -> cat.getID().equals(cat2.getId()))) {
            throw new Exception("They are already friends");
        }
        cat1.MakeFriend(cat2);
        cat2.MakeFriend(cat1);
        _dao.save(cat1);
        _dao.save(cat2);
    }


    @KafkaListener(topics = "get_friends", groupId = "cat-service")
    public Set<CatDTO> GetFriends(Long catId) throws Exception {
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        if (_dao.findById(catId).isEmpty()) {
            throw new Exception("Cat doesnt exist");
        }
        Set<Cat> list = _dao.findById(catId).get().GetFriendsList();
        Set<CatDTO> result = new HashSet<>();
        for (Cat cat : list) {
            result.add(catMapperDTO.MappedToCatDTO(cat));
        }
        kafkaSetTemplate.send("got_friends", result);
        return result;
    }

    @KafkaListener(topics = "find_cat_owner", groupId = "cat-service")
    public OwnerDTO FindOwner(Long id) throws Exception {
        if (_dao.findById(id).isEmpty()) {
            throw new Exception("Cat doesnt exist");
        }
        Cat cat = _dao.findById(id).get();
        OwnerMapperDTO ownerMapperDTO = new OwnerMapperDTO();
        OwnerDTO result = ownerMapperDTO.MappedToOwnerDTO(cat.getOwner());
        kafkaOwnerTemplate.send("found_cat_owner", result);
        return result;
    }

    @KafkaListener(topics = "color_cat", groupId = "cat-service")
    public Set<CatDTO> GetCatsByColor(List<String> data) {
        Set<Cat> list = _dao.findCatsByColor(ColorMapperDTO.mapToCatColor(CatColorDto.valueOf(data.get(1))));
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        Set<CatDTO> result = new HashSet<>();
        for (Cat cat : list) {
            if (( data.get(0) != null) && (Objects.equals(cat.getOwner().getId(), Long.parseLong(data.get(0))))) {
                result.add(catMapperDTO.MappedToCatDTO(cat));
            } else {
                result.add(catMapperDTO.MappedToCatDTO(cat));
            }
        }
        kafkaSetTemplate.send("shown_cat", result);

        return result;
    }

    @KafkaListener(topics = "breed_cat", groupId = "cat-service")
    public Set<CatDTO> GetCatsByBreed(List<String> data) {
        Set<Cat> list = _dao.findCatsByBreed(BreedMapperDTO.mapToCatBreed(CatBreedDto.valueOf(data.get(2))));
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        Set<CatDTO> result = new HashSet<>();
        for (Cat cat : list) {
            if (data.get(0) == null) {
                result.add(catMapperDTO.MappedToCatDTO(cat));
            } else if (Objects.equals(cat.getOwner().getId(), Long.parseLong(data.get(0)))) {
                result.add(catMapperDTO.MappedToCatDTO(cat));
            }
        }
        kafkaSetTemplate.send("shown_cat", result);

        return result;
    }

    @KafkaListener(topics = "show_cat", groupId = "cat-service")
    public Set<CatDTO> ShowCats(List<String> data) {
        List<Cat> list = _dao.findAll();
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        Set<CatDTO> result = new HashSet<>();
        for (Cat cat : list) {
            if (data.get(0) == null) {
                result.add(catMapperDTO.MappedToCatDTO(cat));
            } else {
                if (Objects.equals(cat.getOwner().getId(), Long.parseLong(data.get(0)))) {
                    result.add(catMapperDTO.MappedToCatDTO(cat));
                }
            }
        }
        kafkaSetTemplate.send("shown_cat", result);

        return result;
    }
}
