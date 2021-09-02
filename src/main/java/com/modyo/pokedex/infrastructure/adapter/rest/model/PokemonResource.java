package com.modyo.pokedex.infrastructure.adapter.rest.model;

import com.modyo.pokedex.domain.model.PokemonBasicInfo;
import com.modyo.pokedex.domain.model.PokemonDetailInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PokemonResource implements Serializable {

    private String name;
    private Integer weight;
    private List<PokemonAbility> abilities;
    private List<PokemonType> types;
    private NamedResource species;

    public PokemonBasicInfo toBasicDomain() {
        return PokemonBasicInfo.builder()
                .name(this.name)
                .weight(this.weight)
                .type(fetchType())
                .abilities(fetchAbilities())
                .build();
    }

    public PokemonDetailInfo toDetailDomain(List<String> evolutions) {
        return PokemonDetailInfo.builder()
                .name(this.name)
                .weight(this.weight)
                .type(fetchType())
                .abilities(fetchAbilities())
                .evolutions(evolutions)
                .build();
    }

    private String fetchType() {
        return Optional.ofNullable(this.types).orElse(Collections.emptyList())
                .stream()
                .map(PokemonType::getType)
                .map(NamedResource::getName)
                .collect(Collectors.joining(", "));
    }

    private List<String> fetchAbilities() {
        return Optional.ofNullable(this.abilities).orElse(Collections.emptyList())
                .stream()
                .map(PokemonAbility::getAbility)
                .map(NamedResource::getName)
                .collect(Collectors.toList());
    }
}
