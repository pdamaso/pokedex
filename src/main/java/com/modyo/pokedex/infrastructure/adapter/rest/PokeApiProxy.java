package com.modyo.pokedex.infrastructure.adapter.rest;

import com.modyo.pokedex.infrastructure.adapter.rest.error.SourceApiClientError;
import com.modyo.pokedex.infrastructure.adapter.rest.error.SourceApiServerError;
import com.modyo.pokedex.infrastructure.adapter.rest.model.Characteristic;
import com.modyo.pokedex.infrastructure.adapter.rest.model.EvolutionChain;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonResource;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonResponse;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonSpecies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
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
        log.info("gettingPokemons, url={}", pageableUrl);
        return fetchResource(pageableUrl, PokemonResponse.class);
    }

    @Cacheable("pokemon-resource")
    public PokemonResource getPokemonResource(String name) {
        String resourceUrl = pokemonUrl.concat(name);
        log.info("gettingPokemonResource, name={}, url={}", name, resourceUrl);
        return fetchResource(resourceUrl, PokemonResource.class);
    }

    @Cacheable("pokemon-species")
    public PokemonSpecies getSpecies(String url) {
        log.info("gettingPokemonSpeciesByUrl, url={}", url);
        return fetchResource(url, PokemonSpecies.class);
    }

    @Cacheable("evolution-chain")
    public EvolutionChain getEvolutions(String url) {
        log.info("gettingEvolutionChainByUrl, url={}", url);
        return fetchResource(url, EvolutionChain.class);
    }

    @Cacheable("characteristic")
    public Characteristic getCharacteristic(Long id) {
        String resourceUrl = characteristicUrl.concat(String.valueOf(id));
        log.info("gettingCharacteristicByUrl, url={}", resourceUrl);
        return fetchResource(resourceUrl, Characteristic.class);
    }

    private <T> T fetchResource(String resourceUrl, Class<T> responseType) {
        try {
            ResponseEntity<T> responseEntity = restTemplate.getForEntity(resourceUrl, responseType);
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            log.error("cannotGetResource, url={}, statusCode={}, cause={}", resourceUrl, e.getStatusCode(),
                    e.getMostSpecificCause().getMessage());
            String message = buildErrorMessage(responseType, resourceUrl, e.getMessage());
            throw new SourceApiClientError(message, e.getStatusCode(), e);
        } catch (HttpServerErrorException e) {
            log.error("cannotGetResource, url={}, statusCode={}, cause={}", resourceUrl, e.getStatusCode(),
                    e.getMostSpecificCause().getMessage());
            String message = buildErrorMessage(responseType, resourceUrl, e.getMessage());
            throw new SourceApiServerError(message, e.getStatusCode(), e);
        } catch (RestClientException e) {
            log.error("cannotGetResource, url={}, cause={}", resourceUrl, e.getMostSpecificCause().getMessage());
            String message = buildErrorMessage(responseType, resourceUrl, e.getMessage());
            throw new SourceApiServerError(message, HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private <T> String buildErrorMessage(Class<T> resourceType, String url, String error) {
        return String.format("Cannot resolve type '%s' with url '%s' due to '%s'",
                resourceType.getSimpleName(), url, error);
    }
}
