package com.gabriela.bookmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados para cadastrar ou atualizar um livro")
public record LivroRequest(
        @Schema(description = "Título da obra", example = "Dom Casmurro")
        @NotBlank(message = "O título é obrigatório") String titulo,

        @Schema(description = "ISBN, quando disponível", example = "9788535911664")
        String isbn,

        @Schema(description = "Ano de publicação", example = "1899")
        Integer anoPublicacao,

        @Schema(description = "Gênero ou categoria literária", example = "Romance")
        String genero,

        @Schema(description = "Quantidade de exemplares cadastrados. No cadastro, todos ficam disponíveis; "
                + "na atualização, a diferença é aplicada ao estoque disponível", example = "3")
        @NotNull(message = "A quantidade total é obrigatória")
        @Min(value = 0, message = "A quantidade não pode ser negativa") Integer quantidadeTotal,

        @Schema(description = "ID de um autor já cadastrado", example = "1")
        @NotNull(message = "O autor é obrigatório") Long autorId
) {}