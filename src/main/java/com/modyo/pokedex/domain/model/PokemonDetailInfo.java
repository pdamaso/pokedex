package com.modyo.pokedex.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class PokemonDetailInfo extends PokemonBasicInfo {
    private List<String> evolutions;
}
