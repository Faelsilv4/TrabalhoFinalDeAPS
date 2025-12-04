package edu.uea.trabalho_final_APS_2.service;

import edu.uea.trabalho_final_APS_2.model.Livro;
import edu.uea.trabalho_final_APS_2.model.Status;
import edu.uea.trabalho_final_APS_2.repository.LivroRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Define que esta classe é um componente de Serviço do Spring
@Service 
public class LivroService {

    // Injeta o Repositório, permitindo o acesso ao banco de dados
    @Autowired 
    private LivroRepository livroRepository;

    // --- Métodos de CRUD (Create, Read, Update, Delete) ---

    /**
     * Salva um novo livro ou atualiza um existente.
     */
    @Transactional // Garante que a operação seja atômica
    public Livro salvar(Livro livro) {
        // Se for uma atualização, aqui é um bom lugar para validar se 
        // o status está sendo alterado corretamente, mas manteremos simples por hora.
        return livroRepository.save(livro);
    }

    /**
     * Retorna todos os livros cadastrados.
     */
    public List<Livro> buscarTodos() {
        return livroRepository.findAll();
    }

    /**
     * Busca um livro pelo seu ID.
     */
    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }

    /**
     * Exclui um livro pelo seu ID, mas só se estiver DISPONÍVEL.
     */
    @Transactional // Garante que a operação seja atômica
    public void deletar(Long id) {
        
        Livro livro = livroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Livro não encontrado."));
        
        // 🚨 VALIDAÇÃO DE REGRA DE NEGÓCIO: Só deleta se o status for DISPONIVEL
        if (livro.getStatus() != Status.DISPONIVEL) {
             throw new RuntimeException("Não é possível deletar o livro. Status atual: " 
                 + livro.getStatus() + ". Cancele reservas ou aguarde a devolução.");
        }
        
        livroRepository.deleteById(id);
    }
}