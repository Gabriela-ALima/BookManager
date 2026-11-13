package com.gabriela.bookmanager.service;

import com.gabriela.bookmanager.dto.LivroRequest;
import com.gabriela.bookmanager.dto.LivroResponse;
import com.gabriela.bookmanager.exception.RecursoNaoEncontradoException;
import com.gabriela.bookmanager.exception.RegraNegocioException;
import com.gabriela.bookmanager.model.Autor;
import com.gabriela.bookmanager.model.Livro;
import com.gabriela.bookmanager.repository.EmprestimoRepository;
import com.gabriela.bookmanager.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final AutorService autorService;

    public List<LivroResponse> listar(String titulo, String autor, String genero) {
        List<Livro> livros;
        if (titulo != null && !titulo.isBlank()) {
            livros = livroRepository.findByTituloContainingIgnoreCase(titulo);
        } else if (autor != null && !autor.isBlank()) {
            livros = livroRepository.findByAutorNomeContainingIgnoreCase(autor);
        } else if (genero != null && !genero.isBlank()) {
            livros = livroRepository.findByGeneroContainingIgnoreCase(genero);
        } else {
            livros = livroRepository.findAll();
        }
        return livros.stream().map(LivroResponse::from).toList();
    }

    public LivroResponse buscar(Long id) {
        return LivroResponse.from(buscarEntidade(id));
    }

    Livro buscarEntidade(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado: " + id));
    }

    @Transactional
    public LivroResponse criar(LivroRequest req) {
        Autor autor = autorService.buscarPorId(req.autorId());

        Livro livro = new Livro();
        preencher(livro, req, autor);
        livro.setQuantidadeDisponivel(req.quantidadeTotal());
        return LivroResponse.from(livroRepository.save(livro));
    }

    @Transactional
    public LivroResponse atualizar(Long id, LivroRequest req) {
        Livro livro = buscarEntidade(id);
        Autor autor = autorService.buscarPorId(req.autorId());

        int diferenca = req.quantidadeTotal() - livro.getQuantidadeTotal();
        int novoDisponivel = livro.getQuantidadeDisponivel() + diferenca;
        if (novoDisponivel < 0) {
            throw new RegraNegocioException(
                    "A quantidade total não pode ser menor que a quantidade de exemplares emprestados");
        }

        preencher(livro, req, autor);
        livro.setQuantidadeDisponivel(novoDisponivel);
        return LivroResponse.from(livroRepository.save(livro));
    }

    @Transactional
    public void excluir(Long id) {
        Livro livro = buscarEntidade(id);
        if (emprestimoRepository.existsByLivroId(id)) {
            throw new RegraNegocioException("Não é possível excluir um livro que possui empréstimos registrados");
        }
        livroRepository.delete(livro);
    }

    private void preencher(Livro livro, LivroRequest req, Autor autor) {
        livro.setTitulo(req.titulo());
        livro.setIsbn(req.isbn());
        livro.setAnoPublicacao(req.anoPublicacao());
        livro.setGenero(req.genero());
        livro.setQuantidadeTotal(req.quantidadeTotal());
        livro.setAutor(autor);
    }
}