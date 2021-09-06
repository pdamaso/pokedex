package com.modyo.pokedex.infrastructure.repository.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PokemonSpecies implements Serializable {

    @JsonProperty("evolution_chain")
    private NamedResource evolutionChain;
    @JsonProperty("flavor_text_entries")
    private List<FlavorText> flavorText;

    public String getDescription(String lang) {
        return Optional.ofNullable(flavorText).orElse(Collections.emptyList())
                .stream()
                .filter(text -> text.belongsTo(lang))
                .map(FlavorText::getDescription).findFirst()
                .orElse("");
    }
}
