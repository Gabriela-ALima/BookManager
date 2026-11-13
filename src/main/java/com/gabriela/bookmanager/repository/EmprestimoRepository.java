package com.gabriela.bookmanager.repository;

import com.gabriela.bookmanager.model.Emprestimo;
import com.gabriela.bookmanager.model.StatusEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByStatus(StatusEmprestimo status);

    boolean existsByLivroIdAndStatus(Long livroId, StatusEmprestimo status);

    boolean existsByLivroId(Long livroId);
}