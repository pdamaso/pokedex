package com.modyo.pokedex.infrastructure.repository;

import com.modyo.pokedex.domain.model.BasePokemon;
import com.modyo.pokedex.domain.model.DetailedPokemon;
import com.modyo.pokedex.domain.port.GetPokemonDetailsPort;
import com.modyo.pokedex.domain.port.GetPokemonsPort;
import com.modyo.pokedex.infrastructure.repository.rest.PokeApiProxy;
import com.modyo.pokedex.infrastructure.repository.rest.model.EvolutionChain;
import com.modyo.pokedex.infrastructure.repository.rest.model.PokemonResource;
import com.modyo.pokedex.infrastructure.repository.rest.model.PokemonResponse;
import com.modyo.pokedex.infrastructure.repository.rest.model.PokemonSpecies;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PokeApiAdapter implements GetPokemonsPort, GetPokemonDetailsPort {

    private final PokeApiProxy pokeApiProxy;

    public PokeApiAdapter(PokeApiProxy pokeApiProxy) {
        this.pokeApiProxy = pokeApiProxy;
    }

    @Override
    public List<BasePokemon> getPokemons(long offset, long limit) {
        PokemonResponse pokemonResources = pokeApiProxy.getPokemons(offset, limit);
        return pokemonResources.getResults().parallelStream()
                .map(resource -> pokeApiProxy.getPokemonResource(resource.getName()))
                .map(PokemonResource::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public DetailedPokemon getPokemon(String name) {
        PokemonResource pokemonResource = pokeApiProxy.getPokemonResource(name);
        PokemonSpecies species = getSpecies(pokemonResource);
        List<String> evolutions = getEvolutions(species);
        String description = species.getDescription("en");
        return pokemonResource.toDomain(description, evolutions);
    }

    private PokemonSpecies getSpecies(PokemonResource pokemonResource) {
        String speciesUrl = pokemonResource.getSpecies().getUrl();
        return pokeApiProxy.getSpecies(speciesUrl);
    }

    private List<String> getEvolutions(PokemonSpecies species) {
        String evolutionChainUrl = species.getEvolutionChain().getUrl();
        EvolutionChain evolutionChain = pokeApiProxy.getEvolutions(evolutionChainUrl);
        return evolutionChain.getEvolutions();
    }
}
