package com.modyo.pokedex.infrastructure.presentation.model;

import com.modyo.pokedex.domain.model.BasePokemon;
import com.modyo.pokedex.infrastructure.presentation.PokedexController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ResourceAssembler {

    public RepresentationModel<?> assembleResponse(List<BasePokemon> pokemons, long offset, long limit) {
        List<? extends RepresentationModel<?>> resources = pokemons.stream()
                .map(this::assemblePokemonModel)
                .collect(Collectors.toList());
        RepresentationModel<?> model = RepresentationModel.of(ResponseWrapper.of(resources));
        addLink(model, "prev", offset - limit, limit);
        addLink(model, "self", offset, limit);
        addLink(model, "next", offset + limit, limit);
        return model;
    }

    private void addLink(RepresentationModel<?> model, String relation, long offset, long limit) {
        if (offset >= 0) {
            model.add(WebMvcLinkBuilder.linkTo(methodOn(PokedexController.class).get(offset, limit)).withRel(relation));
        }
    }

    private RepresentationModel<?> assemblePokemonModel(BasePokemon pokemon) {
        RepresentationModel<?> model = RepresentationModel.of(pokemon);
        model.add(linkTo(methodOn(PokedexController.class).get(pokemon.getName())).withSelfRel());
        return model;
    }
}
