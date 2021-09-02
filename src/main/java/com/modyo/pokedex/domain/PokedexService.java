package com.modyo.pokedex.domain;

import com.modyo.pokedex.domain.model.PokemonBasicInfo;
import com.modyo.pokedex.domain.model.PokemonDetailInfo;
import com.modyo.pokedex.domain.port.GetPokemonDetailsPort;
import com.modyo.pokedex.domain.port.GetPokemonsPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PokedexService {

    private final GetPokemonsPort getPokemonsPort;
    private final GetPokemonDetailsPort getPokemonDetailsPort;

    public PokedexService(GetPokemonsPort getPokemonsPort,
                          GetPokemonDetailsPort getPokemonDetailsPort) {
        this.getPokemonsPort = getPokemonsPort;
        this.getPokemonDetailsPort = getPokemonDetailsPort;
    }

    public List<PokemonBasicInfo> getPokemons() {
        return getPokemonsPort.getAll();
    }

    public PokemonDetailInfo getPokemon(String name) {
        return getPokemonDetailsPort.get(name);
    }
}
