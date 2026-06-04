package edu.uea.trabalho_final_APS_2.service;

import edu.uea.trabalho_final_APS_2.model.Livro;
import edu.uea.trabalho_final_APS_2.model.Status;
import edu.uea.trabalho_final_APS_2.repository.LivroRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Transactional
    public Livro salvar(Livro livro) {
        if (livro.getStatus() == null) {
            livro.setStatus(Status.DISPONIVEL);
        }

        return livroRepository.save(livro);
    }

    public List<Livro> buscarTodos() {
        return livroRepository.findAll();
    }

    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }

    @Transactional
    public Livro atualizar(Long id, Livro livroAtualizado) {

        Livro livroExistente = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado."));

        livroExistente.setTitulo(livroAtualizado.getTitulo());
        livroExistente.setAutor(livroAtualizado.getAutor());
        livroExistente.setGenero(livroAtualizado.getGenero());
        livroExistente.setNumPaginas(livroAtualizado.getNumPaginas());
        livroExistente.setAnoDePublicacao(livroAtualizado.getAnoDePublicacao());
        livroExistente.setCategoria(livroAtualizado.getCategoria());
        livroExistente.setUrlCapa(livroAtualizado.getUrlCapa());

        return livroRepository.save(livroExistente);
    }

    @Transactional
    public void deletar(Long id) {

        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado."));

        if (livro.getStatus() != Status.DISPONIVEL) {
            throw new RuntimeException("Não é possível deletar o livro. Status atual: "
                    + livro.getStatus() + ". Aguarde a devolução.");
        }

        livroRepository.deleteById(id);
    }
}