package com.Chub.Services;

import com.Chub.DTOs.CatBreedDto;
import com.Chub.dao.Models.cats.CatBreed;

public class BreedMapperDTO {
    public static CatBreed mapToCatBreed(CatBreedDto catBreedDto) {
        return switch (catBreedDto) {
            case Ragdoll -> CatBreed.Ragdoll;
            case ExoticShorthair -> CatBreed.ExoticShorthair;
            case BritishShorthair -> CatBreed.BritishShorthair;
            case Persian -> CatBreed.Persian;
            case MaineCoon -> CatBreed.MaineCoon;
            case AmericanShorthair -> CatBreed.AmericanShorthair;
            case ScottishFold -> CatBreed.ScottishFold;
            case Oriental -> CatBreed.Oriental;
            case Siamese -> CatBreed.Siamese;
            case CornishRex -> CatBreed.CornishRex;
        };
    }
    public static CatBreedDto mapToCatBreedDto(CatBreed catBreed) {
        return switch (catBreed) {
            case Ragdoll -> CatBreedDto.Ragdoll;
            case ExoticShorthair -> CatBreedDto.ExoticShorthair;
            case BritishShorthair -> CatBreedDto.BritishShorthair;
            case Persian -> CatBreedDto.Persian;
            case MaineCoon -> CatBreedDto.MaineCoon;
            case AmericanShorthair -> CatBreedDto.AmericanShorthair;
            case ScottishFold -> CatBreedDto.ScottishFold;
            case Oriental -> CatBreedDto.Oriental;
            case Siamese -> CatBreedDto.Siamese;
            case CornishRex -> CatBreedDto.CornishRex;
        };
    }
}
