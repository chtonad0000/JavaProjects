package com.Chub.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CatDTO {
    private Long ID;
    private String name;
    private LocalDate birthDate;
    private CatBreedDto breed;
    private CatColorDto color;
    private OwnerDTO owner;
}
