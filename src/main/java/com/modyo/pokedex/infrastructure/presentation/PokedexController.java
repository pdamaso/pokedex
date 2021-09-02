package com.modyo.pokedex.infrastructure.presentation;

import com.modyo.pokedex.domain.PokedexService;
import com.modyo.pokedex.domain.model.BasePokemon;
import com.modyo.pokedex.domain.model.DetailedPokemon;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/pokemon")
public class PokedexController {

    private final PokedexService pokedexService;
    private final ResourceAssembler resourceAssembler;

    public PokedexController(PokedexService pokedexService,
                             ResourceAssembler resourceAssembler) {
        this.pokedexService = pokedexService;
        this.resourceAssembler = resourceAssembler;
    }

    @GetMapping(value = "/")
    public ResponseEntity<RepresentationModel<?>> get(
            @RequestParam(required = false, defaultValue = "0") long offset,
            @RequestParam(required = false, defaultValue = "20") long limit) {

        List<BasePokemon> pokemons = pokedexService.getPokemons(offset, limit);
        RepresentationModel<?> model = resourceAssembler.assembleResponse(pokemons, offset, limit);
        return ResponseEntity.ok(model);
    }

    @GetMapping(value = "/{name}")
    public ResponseEntity<DetailedPokemon> get(@PathVariable String name) {

        DetailedPokemon pokemon = pokedexService.getPokemon(name);
        return ResponseEntity.ok(pokemon);
    }
}
