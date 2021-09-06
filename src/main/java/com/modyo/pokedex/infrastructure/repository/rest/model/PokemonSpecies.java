package com.modyo.pokedex.infrastructure.repository.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PokemonSpecies implements Serializable {

    @JsonProperty("evolution_chain")
    private NamedResource evolutionChain;
}
