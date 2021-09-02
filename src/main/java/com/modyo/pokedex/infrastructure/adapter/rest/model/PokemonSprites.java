package com.modyo.pokedex.infrastructure.adapter.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PokemonSprites implements Serializable {
    @JsonProperty("front_default")
    private String frontDefault;
    @JsonIgnore
    private String frontDetail;

    @JsonProperty("other")
    private void unpackOfficialArtwork(Map<String, Object> other) {
        Map<String, Object> official = ((Map<String, Object>) other.get("official-artwork"));
        this.frontDetail = official.get("front_default").toString();
    }
}
