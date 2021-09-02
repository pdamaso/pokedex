package com.modyo.pokedex.domain.port;

import com.modyo.pokedex.domain.model.DetailedPokemon;

public interface GetPokemonDetailsPort {
    DetailedPokemon get(String name);
}
