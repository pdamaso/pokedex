package com.modyo.pokedex.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.modyo.pokedex.infrastructure.presentation"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        Contact developer = new Contact("Pablo Damaso", "https://www.linkedin.com/in/pdamaso/", "pablo.damaso@gmail.com");
        return new ApiInfoBuilder()
                .title("Pokedex API")
                .description("Modyo Challenge")
                .contact(developer)
                .version("1.0")
                .build();
    }
}
