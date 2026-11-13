package com.gabriela.bookmanager.dto;

import com.gabriela.bookmanager.model.Emprestimo;
import com.gabriela.bookmanager.model.StatusEmprestimo;
import java.time.LocalDate;

public record EmprestimoResponse(
        Long id,
        LocalDate dataEmprestimo,
        LocalDate dataPrevistaDevolucao,
        LocalDate dataDevolucao,
        StatusEmprestimo status,
        Long livroId,
        String livroTitulo
) {
    public static EmprestimoResponse from(Emprestimo e) {
        return new EmprestimoResponse(
                e.getId(), e.getDataEmprestimo(), e.getDataPrevistaDevolucao(),
                e.getDataDevolucao(), e.getStatus(),
                e.getLivro().getId(), e.getLivro().getTitulo());
    }
}