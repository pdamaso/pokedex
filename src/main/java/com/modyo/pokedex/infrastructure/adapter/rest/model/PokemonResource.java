package com.modyo.pokedex.infrastructure.adapter.rest.model;

import com.modyo.pokedex.domain.model.BasePokemon;
import com.modyo.pokedex.domain.model.DetailedPokemon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PokemonResource implements Serializable {

    private Long id;
    private String name;
    private Integer weight;
    private List<PokemonAbility> abilities;
    private List<PokemonType> types;
    private PokemonSprites sprites;
    private NamedResource species;

    public BasePokemon toDomain() {
        return BasePokemon.builder()
                .name(this.name)
                .weight(this.weight)
                .image(fetchImage(PokemonSprites::getFrontDefault))
                .type(fetchType())
                .abilities(fetchAbilities())
                .build();
    }

    public DetailedPokemon toDomain(String description, List<String> evolutions) {
        return DetailedPokemon.builder()
                .name(this.name)
                .weight(this.weight)
                .type(fetchType())
                .image(fetchImage(PokemonSprites::getFrontDetail))
                .abilities(fetchAbilities())
                .description(description)
                .evolutions(evolutions)
                .build();
    }

    private String fetchImage(Function<PokemonSprites, String> spritesFunction) {
        return Optional.ofNullable(this.sprites).map(spritesFunction).orElse("");
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
