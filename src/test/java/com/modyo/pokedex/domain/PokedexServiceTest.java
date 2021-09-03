package com.modyo.pokedex.domain;

import com.modyo.pokedex.domain.port.GetPokemonDetailsPort;
import com.modyo.pokedex.domain.port.GetPokemonsPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PokedexServiceTest {

    @Mock
    private GetPokemonsPort getPokemonsPort;
    @Mock
    private GetPokemonDetailsPort getPokemonDetailsPort;

    @InjectMocks
    private PokedexService pokedexService;

    @Test
    void shouldGetPokemons() {
        pokedexService.getPokemons(0, 20);
        then(getPokemonsPort).should().getPokemons(0, 20);
    }

    @Test
    void shouldGetPokemonByName() {
        pokedexService.getPokemon("name");
        then(getPokemonDetailsPort).should().getPokemon("name");
    }
}