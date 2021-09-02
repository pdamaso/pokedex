package com.modyo.pokedex.infrastructure.adapter;

import com.modyo.pokedex.domain.model.PokemonBasicInfo;
import com.modyo.pokedex.domain.model.PokemonDetailInfo;
import com.modyo.pokedex.domain.port.GetPokemonDetailsPort;
import com.modyo.pokedex.domain.port.GetPokemonsPort;
import com.modyo.pokedex.infrastructure.adapter.rest.PokeApiProxy;
import com.modyo.pokedex.infrastructure.adapter.rest.model.EvolutionChain;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonResource;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonResponse;
import com.modyo.pokedex.infrastructure.adapter.rest.model.PokemonSpecies;
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
    public List<PokemonBasicInfo> getAll() {
        // TODO pagination | offset & limit
        PokemonResponse pokemonResources = pokeApiProxy.getPokemonList();
        return pokemonResources.getResults().stream()
                .map(namedResource -> pokeApiProxy.getPokemonResource(namedResource.getName()))
                .map(PokemonResource::toBasicDomain)
                .collect(Collectors.toList());
    }

    @Override
    public PokemonDetailInfo get(String name) {
        PokemonResource pokemonResource = pokeApiProxy.getPokemonResource(name);
        // TODO description
        List<String> evolutions = getEvolutions(pokemonResource);
        return pokemonResource.toDetailDomain(evolutions);
    }

    private List<String> getEvolutions(PokemonResource pokemonResource) {
        String speciesUrl = pokemonResource.getSpecies().getUrl();
        PokemonSpecies species = pokeApiProxy.getSpecies(speciesUrl);
        String evolutionChainUrl = species.getEvolutionChain().getUrl();
        EvolutionChain evolutionChain = pokeApiProxy.getEvolutions(evolutionChainUrl);
        return evolutionChain.getChain().getEvolutions();
    }

}
