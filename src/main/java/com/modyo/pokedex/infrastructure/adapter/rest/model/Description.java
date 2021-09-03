package com.modyo.pokedex.infrastructure.adapter.rest.model;

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
public class Description implements Serializable {
    private String description;
    private NamedResource language;

    public boolean belongsTo(String lang) {
        return Optional.ofNullable(language)
                .map(namedResource -> lang.equalsIgnoreCase(namedResource.getName()))
                .orElse(false);
    }
}
