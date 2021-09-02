package com.modyo.pokedex.infrastructure.adapter;

import com.modyo.pokedex.domain.model.BasePokemon;
import com.modyo.pokedex.domain.model.DetailedPokemon;
import com.modyo.pokedex.infrastructure.adapter.rest.PokeApiProxy;
import com.modyo.pokedex.infrastructure.adapter.rest.model.ChainLink;
import com.modyo.pokedex.infrastructure.adapter.rest.model.Characteristic;
import com.modyo.pokedex.infrastructure.adapter.rest.model.EvolutionChain;
import com.modyo.pokedex.infrastructure.adapter.rest.model.NamedResource;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonResource;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonResponse;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonSpecies;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PokeApiAdapterTest {

    @Mock
    private PokeApiProxy pokeApiProxy;

    @InjectMocks
    private PokeApiAdapter pokeApiAdapter;

    @Test
    void shouldGetAllPokemons() {

        String aPokemonName = "abc";
        String otherPokemonName = "xyz";

        PokemonResponse pokemonResponse = getPokemonResponse(aPokemonName, otherPokemonName);
        given(pokeApiProxy.getPokemons(0, 20))
                .willReturn(pokemonResponse);

        given(pokeApiProxy.getPokemonResource(aPokemonName))
                .willReturn(getPokemonResource());
        given(pokeApiProxy.getPokemonResource(otherPokemonName))
                .willReturn(getPokemonResource());

        List<BasePokemon> all = pokeApiAdapter.getPokemons(0, 20);

        assertThat(all).hasSize(2);

        then(pokeApiProxy).should(times(2)).getPokemonResource(anyString());
    }

    @Test
    void shouldGetPokemonByName() {

        given(pokeApiProxy.getPokemonResource("name"))
                .willReturn(getPokemonResource());

        given(pokeApiProxy.getSpecies("species-url"))
                .willReturn(getSpecies());

        EvolutionChain evolutionChain = getEvolutionChain();
        given(pokeApiProxy.getEvolutions("evolution-chain-url"))
                .willReturn(evolutionChain);

        given(pokeApiProxy.getCharacteristic(1L))
                .willReturn(Characteristic.builder().build());

        DetailedPokemon pokemon = pokeApiAdapter.get("name");

        assertThat(pokemon.getEvolutions()).containsOnly("some-specie", "another-specie");
    }

    private EvolutionChain getEvolutionChain() {
        ChainLink evolvesTo = ChainLink.builder()
                .species(getNamedResource("another-specie"))
                .evolvesTo(Collections.emptyList())
                .build();
        ChainLink chainLink = ChainLink.builder()
                .species(getNamedResource("some-specie"))
                .evolvesTo(Collections.singletonList(evolvesTo))
                .build();
        return EvolutionChain.builder()
                .chain(chainLink)
                .build();
    }

    private PokemonSpecies getSpecies() {
        return PokemonSpecies.builder()
                .evolutionChain(getNamedResource("", "evolution-chain-url"))
                .build();
    }

    private PokemonResource getPokemonResource() {
        return PokemonResource.builder()
                .id(1L)
                .species(getNamedResource("some-specie", "species-url"))
                .build();
    }

    private PokemonResponse getPokemonResponse(String firstName, String secondName) {
        List<NamedResource> results =
                Arrays.asList(getNamedResource(firstName), getNamedResource(secondName));
        return PokemonResponse.builder()
                .results(results)
                .build();
    }

    private NamedResource getNamedResource(String name) {
        return getNamedResource(name, "some-url");
    }

    private NamedResource getNamedResource(String name, String url) {
        return NamedResource.builder()
                .name(name)
                .url(url)
                .build();
    }
}