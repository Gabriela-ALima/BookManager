package com.gabriela.bookmanager.service;

import com.gabriela.bookmanager.dto.EmprestimoRequest;
import com.gabriela.bookmanager.dto.EmprestimoResponse;
import com.gabriela.bookmanager.exception.RecursoNaoEncontradoException;
import com.gabriela.bookmanager.exception.RegraNegocioException;
import com.gabriela.bookmanager.model.Emprestimo;
import com.gabriela.bookmanager.model.Livro;
import com.gabriela.bookmanager.model.StatusEmprestimo;
import com.gabriela.bookmanager.repository.EmprestimoRepository;
import com.gabriela.bookmanager.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;

    public List<EmprestimoResponse> listar(StatusEmprestimo status) {
        List<Emprestimo> lista = (status == null)
                ? emprestimoRepository.findAll()
                : emprestimoRepository.findByStatus(status);
        return lista.stream().map(EmprestimoResponse::from).toList();
    }

    @Transactional
    public EmprestimoResponse registrar(EmprestimoRequest req) {
        Livro livro = livroRepository.findById(req.livroId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado: " + req.livroId()));

        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new RegraNegocioException("Não há exemplares disponíveis deste livro para empréstimo");
        }

        LocalDate hoje = LocalDate.now();
        if (req.dataPrevistaDevolucao().isBefore(hoje)) {
            throw new RegraNegocioException("A data prevista de devolução não pode ser anterior à data de hoje");
        }

        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        livroRepository.save(livro);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(hoje);
        emprestimo.setDataPrevistaDevolucao(req.dataPrevistaDevolucao());
        emprestimo.setStatus(StatusEmprestimo.ATIVO);

        return EmprestimoResponse.from(emprestimoRepository.save(emprestimo));
    }

    @Transactional
    public EmprestimoResponse devolver(Long id) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Empréstimo não encontrado: " + id));

        if (emprestimo.getStatus() == StatusEmprestimo.DEVOLVIDO) {
            throw new RegraNegocioException("Este empréstimo já foi devolvido");
        }

        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);
        emprestimo.setDataDevolucao(LocalDate.now());

        Livro livro = emprestimo.getLivro();
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
        livroRepository.save(livro);

        return EmprestimoResponse.from(emprestimoRepository.save(emprestimo));
    }
}