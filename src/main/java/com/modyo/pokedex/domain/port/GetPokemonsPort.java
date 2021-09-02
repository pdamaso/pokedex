package com.modyo.pokedex.domain.port;

import com.modyo.pokedex.domain.model.BasePokemon;

import java.util.List;

public interface GetPokemonsPort {
    List<BasePokemon> getPokemons(long offset, long limit);
}
