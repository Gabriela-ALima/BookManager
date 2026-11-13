package com.gabriela.bookmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bookManagerOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("BookManager API")
                .version("1.0.0")
                .description("""
                        API REST para gerenciamento de livros, autores e empréstimos de um pequeno acervo.

                        **Regra de negócio:** não é possível emprestar um livro sem exemplares disponíveis. \
                        O empréstimo reduz a quantidade disponível em 1 e a devolução aumenta em 1."""));
    }
}