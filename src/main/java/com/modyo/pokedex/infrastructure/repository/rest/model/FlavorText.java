package com.modyo.pokedex.infrastructure.repository.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlavorText implements Serializable {
    @JsonProperty("flavor_text")
    private String description;
    private NamedResource language;

    public boolean belongsTo(String lang) {
        return Optional.ofNullable(language)
                .map(namedResource -> lang.equalsIgnoreCase(namedResource.getName()))
                .orElse(false);
    }
}
