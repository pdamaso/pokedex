package com.modyo.pokedex.domain.port;

import com.modyo.pokedex.domain.model.PokemonDetailInfo;

public interface GetPokemonDetailsPort {
    PokemonDetailInfo get(String name);
}
