package com.modyo.pokedex.infrastructure.presentation;

import com.modyo.pokedex.domain.PokedexService;
import com.modyo.pokedex.domain.model.BasePokemon;
import com.modyo.pokedex.domain.model.DetailedPokemon;
import com.modyo.pokedex.infrastructure.repository.rest.error.SourceApiClientError;
import com.modyo.pokedex.infrastructure.repository.rest.error.SourceApiServerError;
import com.modyo.pokedex.infrastructure.presentation.model.ResourceAssembler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = PokedexController.class)
@Import(ResourceAssembler.class)
class PokedexControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PokedexService pokedexService;
    @Autowired
    private ResourceAssembler resourceAssembler;

    @Test
    void shouldGetPokemonByName() throws Exception {

        DetailedPokemon bulbasaur = DetailedPokemon.builder()
                .name("bulbasaur")
                .type("grass, poison")
                .weight(69)
                .image("some-image-url")
                .abilities(Arrays.asList("overgrow", "chlorophyll"))
                .description("Loves to eat")
                .evolutions(Arrays.asList("bulbasaur", "ivysaur", "venusaur"))
                .build();

        given(pokedexService.getPokemon("bulbasaur"))
                .willReturn(bulbasaur);

        ResultActions resultActions = mockMvc.perform(get("/pokemon/bulbasaur"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$.name").value("bulbasaur"))
                .andExpect(jsonPath("$.type").value("grass, poison"))
                .andExpect(jsonPath("$.weight").value(69))
                .andExpect(jsonPath("$.image").value("some-image-url"))
                .andExpect(jsonPath("$.abilities").exists())
                .andExpect(jsonPath("$.abilities.length()").value(2))
                .andExpect(jsonPath("$.abilities[0]").value("overgrow"))
                .andExpect(jsonPath("$.abilities[1]").value("chlorophyll"))
                .andExpect(jsonPath("$.description").value("Loves to eat"))
                .andExpect(jsonPath("$.evolutions").exists())
                .andExpect(jsonPath("$.evolutions.length()").value(3))
                .andExpect(jsonPath("$.evolutions[0]").value("bulbasaur"))
                .andExpect(jsonPath("$.evolutions[1]").value("ivysaur"))
                .andExpect(jsonPath("$.evolutions[2]").value("venusaur"));
    }

    @Test
    void shouldGetPokemonByNameWhenEmptyFields() throws Exception {

        DetailedPokemon empty = DetailedPokemon.builder()
                .name("empty")
                .build();

        given(pokedexService.getPokemon("empty"))
                .willReturn(empty);

        ResultActions resultActions = mockMvc.perform(get("/pokemon/empty"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$.name").value("empty"))
                .andExpect(jsonPath("$.type").isEmpty())
                .andExpect(jsonPath("$.weight").isEmpty())
                .andExpect(jsonPath("$.image").isEmpty())
                .andExpect(jsonPath("$.abilities").isEmpty())
                .andExpect(jsonPath("$.description").isEmpty())
                .andExpect(jsonPath("$.evolutions").isEmpty());
    }

    @Test
    void shouldGetPokemons() throws Exception {

        BasePokemon pokemon = BasePokemon.builder()
                .name("bulbasaur")
                .type("grass, poison")
                .weight(69)
                .image("some-default-image-url")
                .abilities(Arrays.asList("overgrow", "chlorophyll"))
                .build();

        given(pokedexService.getPokemons(0, 20))
                .willReturn(Collections.singletonList(pokemon));

        ResultActions resultActions = mockMvc.perform(get("/pokemon/"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("bulbasaur"))
                .andExpect(jsonPath("$.content[0].type").value("grass, poison"))
                .andExpect(jsonPath("$.content[0].weight").value(69))
                .andExpect(jsonPath("$.content[0].image").value("some-default-image-url"))
                .andExpect(jsonPath("$.content[0].abilities").exists())
                .andExpect(jsonPath("$.content[0].abilities.length()").value(2))
                .andExpect(jsonPath("$.content[0].abilities[0]").value("overgrow"))
                .andExpect(jsonPath("$.content[0].abilities[1]").value("chlorophyll"))
                .andExpect(jsonPath("$.content[0].description").doesNotExist())
                .andExpect(jsonPath("$.content[0].evolutions").doesNotExist())
                .andExpect(jsonPath("$.content[0]._links").exists())
                .andExpect(jsonPath("$.content[0]._links.self").exists())
                .andExpect(jsonPath("$.content[0]._links.self.href").value("http://localhost/pokemon/bulbasaur"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/pokemon/?offset=0&limit=20"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.next").exists())
                .andExpect(jsonPath("$._links.next.href").value("http://localhost/pokemon/?offset=20&limit=20"));
    }

    @Test
    void shouldGetProperPaginationResponse() throws Exception {

        given(pokedexService.getPokemons(100, 20))
                .willReturn(Collections.emptyList());

        ResultActions resultActions = mockMvc.perform(get("/pokemon/")
                .queryParam("offset", "100")
                .queryParam("limit", "20"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$._links.prev.href").value("http://localhost/pokemon/?offset=80&limit=20"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/pokemon/?offset=100&limit=20"))
                .andExpect(jsonPath("$._links.next.href").value("http://localhost/pokemon/?offset=120&limit=20"));
    }


    @Test
    void shouldHandleClientErrorException() throws Exception {

        SourceApiClientError exception = new SourceApiClientError("resource not found", HttpStatus.NOT_FOUND,
                new HttpClientErrorException(HttpStatus.NOT_FOUND));
        given(pokedexService.getPokemon("client-error"))
                .willThrow(exception);

        ResultActions resultActions = mockMvc.perform(get("/pokemon/client-error"))
                .andExpect(status().isNotFound());

        resultActions
                .andExpect(jsonPath("$.path").value("/pokemon/client-error"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("404 NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("resource not found"));
    }

    @Test
    void shouldHandleServerErrorException() throws Exception {

        SourceApiServerError exception = new SourceApiServerError("service unavailable", HttpStatus.SERVICE_UNAVAILABLE,
                new HttpClientErrorException(HttpStatus.SERVICE_UNAVAILABLE));
        given(pokedexService.getPokemon("server-error"))
                .willThrow(exception);

        ResultActions resultActions = mockMvc.perform(get("/pokemon/server-error"))
                .andExpect(status().isServiceUnavailable());

        resultActions
                .andExpect(jsonPath("$.path").value("/pokemon/server-error"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("503 SERVICE_UNAVAILABLE"))
                .andExpect(jsonPath("$.message").value("service unavailable"));
    }

    @Test
    void shouldHandleException() throws Exception {

        given(pokedexService.getPokemon("wtf"))
                .willThrow(new RuntimeException("something's wrong here!"));

        ResultActions resultActions = mockMvc.perform(get("/pokemon/wtf"))
                .andExpect(status().isInternalServerError());

        resultActions
                .andExpect(jsonPath("$.path").value("/pokemon/wtf"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("500 INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("something's wrong here!"));
    }
}