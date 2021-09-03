package com.modyo.pokedex.infrastructure.adapter.rest.model;

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
public class Characteristic implements Serializable {
    private List<Description> descriptions;

    public String fetchDescription(String language) {
        return Optional.ofNullable(descriptions).orElse(Collections.emptyList())
                .stream()
                .filter(description -> description.belongsTo(language))
                .map(Description::getDescription)
                .findFirst().orElse("");
    }
}
