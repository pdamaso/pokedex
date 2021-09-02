package com.modyo.pokedex.infrastructure.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(staticName = "of")
public class ResponseWrapper extends RepresentationModel<ResponseWrapper> {
    private List<?> content;
}
