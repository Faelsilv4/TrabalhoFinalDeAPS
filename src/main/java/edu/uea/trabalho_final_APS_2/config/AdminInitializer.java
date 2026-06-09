package edu.uea.trabalho_final_APS_2.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uea.trabalho_final_APS_2.dto.LivroPadraoDTO;
import edu.uea.trabalho_final_APS_2.model.Bibliotecario;
import edu.uea.trabalho_final_APS_2.model.Livro;
import edu.uea.trabalho_final_APS_2.model.Role;
import edu.uea.trabalho_final_APS_2.model.Status;
import edu.uea.trabalho_final_APS_2.repository.LivroRepository;
import edu.uea.trabalho_final_APS_2.repository.UsuarioRepository;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public AdminInitializer(
            UsuarioRepository usuarioRepository,
            LivroRepository livroRepository,
            PasswordEncoder passwordEncoder,
            ObjectMapper objectMapper) {
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) {
        criarAdminPadrao();
        criarLivrosPadrao();
    }

    private void criarAdminPadrao() {
        String emailAdmin = "admin@biblioteca.com";

        if (!usuarioRepository.existsByEmail(emailAdmin)) {
            Bibliotecario admin = Bibliotecario.builder()
                    .nome("Bibliotecário Administrador")
                    .email(emailAdmin)
                    .senha(passwordEncoder.encode("admin123"))
                    .role(Role.ROLE_ADMIN)
                    .ativo(true)
                    .anoDeContratacao(LocalDate.now())
                    .build();

            usuarioRepository.save(admin);

            System.out.println("ADMIN padrão criado com sucesso.");
            System.out.println("Email: admin@biblioteca.com");
            System.out.println("Senha: admin123");
        }
    }

    private void criarLivrosPadrao() {
        if (livroRepository.count() > 0) {
            return;
        }

        try {
            InputStream inputStream = getClass()
                    .getResourceAsStream("/livros-padrao.json");

            if (inputStream == null) {
                System.out.println("Arquivo livros-padrao.json não encontrado.");
                return;
            }

            List<LivroPadraoDTO> livrosDTO = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<LivroPadraoDTO>>() {
                    });

            List<Livro> livros = livrosDTO.stream()
                    .map(this::converterParaLivro)
                    .toList();

            livroRepository.saveAll(livros);

            System.out.println("Livros padrão cadastrados com sucesso.");

        } catch (Exception e) {
            System.out.println("Erro ao carregar livros padrão: " + e.getMessage());
        }
    }

    private Livro converterParaLivro(LivroPadraoDTO dto) {
        Livro livro = new Livro();

        livro.setTitulo(dto.getTitulo());
        livro.setAutor(dto.getAutor());
        livro.setGenero(dto.getGenero());
        livro.setNumPaginas(dto.getNumPaginas());
        livro.setAnoDePublicacao(LocalDate.parse(dto.getAnoDePublicacao()));
        livro.setCategoria(dto.getCategoria());
        livro.setUrlCapa(dto.getUrlCapa());
        livro.setStatus(Status.DISPONIVEL);

        return livro;
    }
}