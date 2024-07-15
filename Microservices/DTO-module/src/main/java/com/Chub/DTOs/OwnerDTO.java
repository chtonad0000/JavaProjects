package com.Chub.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OwnerDTO {
    private Long ID;
    private String name;
    private LocalDate birthDate;
}
