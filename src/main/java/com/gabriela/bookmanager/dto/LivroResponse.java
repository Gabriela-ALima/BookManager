package com.gabriela.bookmanager.dto;

import com.gabriela.bookmanager.model.Livro;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Livro cadastrado, com o nome do autor")
public record LivroResponse(
        @Schema(description = "Identificador do livro", example = "1") Long id,
        @Schema(description = "Título da obra", example = "Dom Casmurro") String titulo,
        @Schema(description = "ISBN", example = "9788535911664") String isbn,
        @Schema(description = "Ano de publicação", example = "1899") Integer anoPublicacao,
        @Schema(description = "Gênero ou categoria", example = "Romance") String genero,
        @Schema(description = "Exemplares cadastrados", example = "3") Integer quantidadeTotal,
        @Schema(description = "Exemplares disponíveis para empréstimo", example = "2") Integer quantidadeDisponivel,
        @Schema(description = "ID do autor", example = "1") Long autorId,
        @Schema(description = "Nome do autor", example = "Machado de Assis") String autorNome
) {
    public static LivroResponse from(Livro l) {
        return new LivroResponse(
                l.getId(), l.getTitulo(), l.getIsbn(), l.getAnoPublicacao(),
                l.getGenero(), l.getQuantidadeTotal(), l.getQuantidadeDisponivel(),
                l.getAutor().getId(), l.getAutor().getNome());
    }
}