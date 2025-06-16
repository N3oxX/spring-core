package com.template.spring.core.infrastructure.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Core")
                        .description("Spring default CRUD boilerplate")
                        .version("1.0")
                        .contact(new Contact()
                                .name("David Martinez Perez")
                                .url("https://github.com/N3oxX")
                                .email("davidmn208@gmail.com")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Spring Documentation")
                        .url("https://spring.io/projects/spring-boot"));
    }
}