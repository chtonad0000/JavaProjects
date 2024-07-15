package com.Chub.dto.mapper;

import com.Chub.DTOs.CatDTO;
import com.Chub.Entities.Cat;

public class CatMapperDTO {
    public Cat MappedToCat(CatDTO dto) {
        OwnerMapperDTO ownerMapperDTO = new OwnerMapperDTO();
        return new Cat(dto.getID(), dto.getName(), dto.getBirthDate(), BreedMapperDTO.mapToCatBreed(dto.getBreed()), ColorMapperDTO.mapToCatColor(dto.getColor()), ownerMapperDTO.MappedToOwner(dto.getOwner()));
    }

    public CatDTO MappedToCatDTO(Cat cat) {
        CatDTO dto = new CatDTO();
        dto.setID(cat.getId());
        dto.setName(cat.getName());
        dto.setBirthDate(cat.getBirth_date());
        dto.setColor(ColorMapperDTO.mapToCatColorDTO(cat.getColor()));
        dto.setBreed(BreedMapperDTO.mapToCatBreedDto(cat.getBreed()));
        OwnerMapperDTO ownerMapperDTO = new OwnerMapperDTO();
        dto.setOwner(ownerMapperDTO.MappedToOwnerDTO(cat.getOwner()));

        return dto;
    }
}
