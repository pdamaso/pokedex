package com.modyo.pokedex.infrastructure.adapter.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PokemonResponse implements Serializable {

    private Integer count;
    private String next;
    private String previous;
    private List<NamedResource> results;
}
