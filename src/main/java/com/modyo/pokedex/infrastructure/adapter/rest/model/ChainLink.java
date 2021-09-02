package com.modyo.pokedex.infrastructure.adapter.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChainLink implements Serializable {

    private NamedResource species;
    @JsonProperty("evolves_to")
    private List<ChainLink> evolvesTo;

    public List<String> getEvolutions() {
        List<String> evolutions = new ArrayList<>();
        evolutions.add(species.getName());
        Optional.ofNullable(evolvesTo).orElse(Collections.emptyList())
                .forEach(chainLink -> evolutions.addAll(chainLink.getEvolutions()));
        return evolutions;
    }
}
