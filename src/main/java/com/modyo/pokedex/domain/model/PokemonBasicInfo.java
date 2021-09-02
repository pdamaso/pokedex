package com.modyo.pokedex.domain.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
public class PokemonBasicInfo {
    private String name;
    private String type;
    private Integer weight;
    private String image;
    private List<String> abilities;
}
