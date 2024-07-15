package com.Chub.dto.mapper;



import com.Chub.DTOs.CatColorDto;
import com.Chub.Models.CatColor;


public class ColorMapperDTO {
    public static CatColor mapToCatColor(CatColorDto catColorDto) {
        return switch (catColorDto) {
            case Brown -> CatColor.Brown;
            case Black -> CatColor.Black;
            case White -> CatColor.White;
            case Orange -> CatColor.Orange;
            case Grey -> CatColor.Grey;
        };
    }

    public static CatColorDto mapToCatColorDTO(CatColor catColor) {
        return switch (catColor) {
            case Brown -> CatColorDto.Brown;
            case Black -> CatColorDto.Black;
            case White -> CatColorDto.White;
            case Orange -> CatColorDto.Orange;
            case Grey -> CatColorDto.Grey;
        };
    }
}