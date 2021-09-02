package com.modyo.pokedex.infrastructure.adapter.rest;

import com.modyo.pokedex.infrastructure.adapter.rest.model.Characteristic;
import com.modyo.pokedex.infrastructure.adapter.rest.model.EvolutionChain;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonResource;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonResponse;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonSpecies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class PokeApiProxy {

    private final RestTemplate restTemplate;
    private final String pokemonUrl;
    private final String characteristicUrl;

    public PokeApiProxy(RestTemplate restTemplate,
                        @Value("${pokeapi.pokemon.url}") String pokemonUrl,
                        @Value("${pokeapi.characteristic.url}") String characteristicUrl) {
        this.restTemplate = restTemplate;
        this.pokemonUrl = pokemonUrl;
        this.characteristicUrl = characteristicUrl;
    }

    public PokemonResponse getPokemons(long offset, long limit) {
        String pageableUrl = UriComponentsBuilder.fromHttpUrl(pokemonUrl)
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .build().toUriString();
        ResponseEntity<PokemonResponse> responseEntity = restTemplate.getForEntity(pageableUrl, PokemonResponse.class);
        return responseEntity.getBody();
    }

    @Cacheable("pokemon-resource")
    public PokemonResource getPokemonResource(String name) {
        String resourceUrl = pokemonUrl.concat(name);
        log.info("gettingPokemonResource, name={}, url={}", name, resourceUrl);
        ResponseEntity<PokemonResource> responseEntity = restTemplate.getForEntity(resourceUrl, PokemonResource.class);
        return responseEntity.getBody();
    }

    @Cacheable("pokemon-species")
    public PokemonSpecies getSpecies(String url) {
        log.info("gettingPokemonSpeciesByUrl, url={}", url);
        ResponseEntity<PokemonSpecies> responseEntity = restTemplate.getForEntity(url, PokemonSpecies.class);
        return responseEntity.getBody();
    }

    @Cacheable("evolution-chain")
    public EvolutionChain getEvolutions(String url) {
        log.info("gettingEvolutionChainByUrl, url={}", url);
        ResponseEntity<EvolutionChain> responseEntity = restTemplate.getForEntity(url, EvolutionChain.class);
        return responseEntity.getBody();
    }

    @Cacheable("characteristic")
    public Characteristic getCharacteristic(Long id) {
        String resourceUrl = characteristicUrl.concat(String.valueOf(id));
        log.info("gettingCharacteristicByUrl, url={}", resourceUrl);
        ResponseEntity<Characteristic> responseEntity = restTemplate.getForEntity(resourceUrl, Characteristic.class);
        return responseEntity.getBody();
    }
}
