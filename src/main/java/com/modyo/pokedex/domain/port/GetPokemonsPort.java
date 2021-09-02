package com.modyo.pokedex.domain.port;

import com.modyo.pokedex.domain.model.PokemonBasicInfo;

import java.util.List;

public interface GetPokemonsPort {
    List<PokemonBasicInfo> getAll();
}
