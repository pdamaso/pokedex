package com.modyo.pokedex.infrastructure.adapter.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvolutionChain implements Serializable {

    private ChainLink chain;
}