package com.Chub.Services;

import com.Chub.DTOs.CatBreedDto;
import com.Chub.DTOs.CatColorDto;
import com.Chub.DTOs.CatDTO;
import com.Chub.DTOs.OwnerDTO;
import com.Chub.dao.Models.cats.Cat;
import com.Chub.dao.repositories.CatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class CatService {
    private final CatRepository _dao;

    @Autowired
    public CatService(CatRepository catDao) {
        _dao = catDao;
    }

    public CatDTO AddCat(CatDTO cat) throws Exception {
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        if (_dao.findById(cat.getID()).isPresent()) {
            throw new Exception("This cat has already added");
        }
        return catMapperDTO.MappedToCatDTO(_dao.save(catMapperDTO.MappedToCat(cat)));
    }

    public void RemoveCat(CatDTO cat) throws Exception {
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        if (_dao.findById(cat.getID()).isEmpty()) {
            throw new Exception("This cat has already removed");
        }
        _dao.delete(catMapperDTO.MappedToCat(cat));
    }

    public void UpdateCat(CatDTO cat) throws Exception {
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        if (_dao.findById(cat.getID()).isEmpty()) {
            throw new Exception("This cat doesn't exist");
        }
        _dao.save(catMapperDTO.MappedToCat(cat));
    }

    public CatDTO FindCatByID(Long id) {
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        if (_dao.findById(id).isEmpty()) {
            return null;
        }
        return catMapperDTO.MappedToCatDTO(_dao.findById(id).get());
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

    public Set<CatDTO> GetFriends(Long catId) throws Exception {
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        if (_dao.findById(catId).isEmpty()) {
            throw new Exception("Cat doesnt exist");
        }
        Set<Cat> list = _dao.findById(catId).get().GetFriendsList();
        Set<CatDTO> result = new HashSet<>();
        for (Cat cat: list) {
            result.add(catMapperDTO.MappedToCatDTO(cat));
        }

        return result;
    }

    public OwnerDTO FindOwner(Long id) throws Exception {
        if (_dao.findById(id).isEmpty()) {
            throw new Exception("Cat doesnt exist");
        }
        Cat cat = _dao.findById(id).get();
        OwnerMapperDTO ownerMapperDTO = new OwnerMapperDTO();
        return ownerMapperDTO.MappedToOwnerDTO(cat.getOwner());
    }

    public Set<CatDTO> GetCatsByColor(CatColorDto color, Long ownerID) {
        Set<Cat> list = _dao.findCatsByColor(ColorMapperDTO.mapToCatColor(color));
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        Set<CatDTO> result = new HashSet<>();
        for (Cat cat: list) {
            if (Objects.equals(cat.getOwner().getId(), ownerID) && ownerID!=null) {
                result.add(catMapperDTO.MappedToCatDTO(cat));
            } else {
                result.add(catMapperDTO.MappedToCatDTO(cat));
            }
        }

        return result;
    }

    public Set<CatDTO> GetCatsByBreed(CatBreedDto breed, Long ownerID) {
        Set<Cat> list = _dao.findCatsByBreed(BreedMapperDTO.mapToCatBreed(breed));
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        Set<CatDTO> result = new HashSet<>();
        for (Cat cat: list) {
            if (Objects.equals(cat.getOwner().getId(), ownerID) && ownerID!=null) {
                result.add(catMapperDTO.MappedToCatDTO(cat));
            } else {
                result.add(catMapperDTO.MappedToCatDTO(cat));
            }
        }

        return result;
    }

    public Set<CatDTO> ShowCats(Long ownerID) {
        List<Cat> list = _dao.findAll();
        CatMapperDTO catMapperDTO = new CatMapperDTO();
        Set<CatDTO> result = new HashSet<>();
        for (Cat cat: list) {
            if (ownerID == null) {
                result.add(catMapperDTO.MappedToCatDTO(cat));
            } else {
                if (Objects.equals(cat.getOwner().getId(), ownerID)) {
                    result.add(catMapperDTO.MappedToCatDTO(cat));
                }
            }
        }

        return result;
    }
}
