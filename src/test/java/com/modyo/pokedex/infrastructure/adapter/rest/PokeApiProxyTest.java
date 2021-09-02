package com.modyo.pokedex.infrastructure.adapter.rest;

import com.modyo.pokedex.infrastructure.adapter.rest.model.EvolutionChain;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonResource;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonResponse;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonSpecies;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PokeApiProxyTest {

    private PokeApiProxy pokeApiProxy;
    @Mock
    private RestTemplate restTemplate;
    private String url;

    @BeforeEach
    void setUp() {
        url = "http://some-url/";
        pokeApiProxy = new PokeApiProxy(restTemplate, url);
    }

    @Test
    void shouldGetPokemons() {
        String pageableUrl = url.concat("?offset=0&limit=20");
        given(restTemplate.getForEntity(pageableUrl, PokemonResponse.class))
                .willReturn(ResponseEntity.ok().build());
        pokeApiProxy.getPokemons(0, 20);
        then(restTemplate).should().getForEntity(pageableUrl, PokemonResponse.class);
    }

    @Test
    void shouldGetPokemon() {
        given(restTemplate.getForEntity(url.concat("name"), PokemonResource.class))
                .willReturn(ResponseEntity.ok().build());
        pokeApiProxy.getPokemonResource("name");
        then(restTemplate).should().getForEntity(url.concat("name"), PokemonResource.class);
    }

    @Test
    void shouldGetSpecies() {
        String resourceUrl = "some-url";
        given(restTemplate.getForEntity(resourceUrl, PokemonSpecies.class))
                .willReturn(ResponseEntity.ok().build());
        pokeApiProxy.getSpecies(resourceUrl);
        then(restTemplate).should().getForEntity(resourceUrl, PokemonSpecies.class);
    }

    @Test
    void shouldGetEvolutions() {
        String resourceUrl = "some-url";
        given(restTemplate.getForEntity(resourceUrl, EvolutionChain.class))
                .willReturn(ResponseEntity.ok().build());
        pokeApiProxy.getEvolutions(resourceUrl);
        then(restTemplate).should().getForEntity(resourceUrl, EvolutionChain.class);
    }
}