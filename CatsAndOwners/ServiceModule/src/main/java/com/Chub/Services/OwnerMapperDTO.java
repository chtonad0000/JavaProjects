package com.Chub.Services;

import com.Chub.DTOs.OwnerDTO;
import com.Chub.dao.Models.owners.Owner;

public class OwnerMapperDTO {
    public Owner MappedToOwner(OwnerDTO dto) {
        return new Owner(dto.getID(), dto.getName(), dto.getBirthDate());
    }

    public OwnerDTO MappedToOwnerDTO(Owner owner) {
        OwnerDTO dto = new OwnerDTO();
        dto.setID(owner.getId());
        dto.setName(owner.getName());
        dto.setBirthDate(owner.getBirth_date());

        return dto;
    }
}
