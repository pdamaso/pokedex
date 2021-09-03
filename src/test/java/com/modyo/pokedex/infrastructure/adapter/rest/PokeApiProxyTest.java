package com.modyo.pokedex.infrastructure.adapter.rest;

import com.modyo.pokedex.infrastructure.adapter.rest.error.SourceApiClientError;
import com.modyo.pokedex.infrastructure.adapter.rest.error.SourceApiServerError;
import com.modyo.pokedex.infrastructure.adapter.rest.model.Characteristic;
import com.modyo.pokedex.infrastructure.adapter.rest.model.EvolutionChain;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonResource;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonResponse;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonSpecies;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PokeApiProxyTest {

    private PokeApiProxy pokeApiProxy;
    @Mock
    private RestTemplate restTemplate;
    private String pokemonUrl;
    private String characteristicUrl;

    @BeforeEach
    void setUp() {
        pokemonUrl = "http://some-url/poke/";
        characteristicUrl = "http://some-url/char/";
        pokeApiProxy = new PokeApiProxy(restTemplate, pokemonUrl, characteristicUrl);
    }

    @Test
    void shouldGetPokemons() {
        String pageableUrl = pokemonUrl.concat("?offset=0&limit=20");
        given(restTemplate.getForEntity(pageableUrl, PokemonResponse.class))
                .willReturn(ResponseEntity.ok().build());
        pokeApiProxy.getPokemons(0, 20);
        then(restTemplate).should().getForEntity(pageableUrl, PokemonResponse.class);
    }

    @Test
    void shouldGetPokemon() {
        given(restTemplate.getForEntity(pokemonUrl.concat("name"), PokemonResource.class))
                .willReturn(ResponseEntity.ok().build());
        pokeApiProxy.getPokemonResource("name");
        then(restTemplate).should().getForEntity(pokemonUrl.concat("name"), PokemonResource.class);
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

    @Test
    void shouldGetCharacteristic() {
        given(restTemplate.getForEntity(characteristicUrl.concat("123"), Characteristic.class))
                .willReturn(ResponseEntity.ok().build());
        pokeApiProxy.getCharacteristic(123L);
        then(restTemplate).should().getForEntity(characteristicUrl.concat("123"), Characteristic.class);
    }

    @Test
    void shouldHandle4xxErrorWhenFetchingResource() {
        given(restTemplate.getForEntity(anyString(), any()))
                .willThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        SourceApiClientError clientError = assertThrows(SourceApiClientError.class,
                () -> pokeApiProxy.getPokemonResource("name"));

        assertThat(clientError).hasMessage("Cannot resolve type 'PokemonResource' with url 'http://some-url/poke/name' due to '404 NOT_FOUND'");
        assertThat(clientError.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldHandle5xxErrorWhenFetchingResource() {
        given(restTemplate.getForEntity(anyString(), any()))
                .willThrow(new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE));

        SourceApiServerError clientError = assertThrows(SourceApiServerError.class,
                () -> pokeApiProxy.getPokemonResource("name"));

        assertThat(clientError).hasMessage("Cannot resolve type 'PokemonResource' with url 'http://some-url/poke/name' due to '503 SERVICE_UNAVAILABLE'");
        assertThat(clientError.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    void shouldHandleRestClientException() {
        given(restTemplate.getForEntity(anyString(), any()))
                .willThrow(new RestClientException("connection reset"));

        SourceApiServerError clientError = assertThrows(SourceApiServerError.class,
                () -> pokeApiProxy.getPokemonResource("name"));

        assertThat(clientError).hasMessage("Cannot resolve type 'PokemonResource' with url 'http://some-url/poke/name' due to 'connection reset'");
        assertThat(clientError.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}