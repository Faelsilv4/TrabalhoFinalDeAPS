package edu.uea.trabalho_final_APS_2.config;

import edu.uea.trabalho_final_APS_2.model.Bibliotecario;
import edu.uea.trabalho_final_APS_2.model.Livro;
import edu.uea.trabalho_final_APS_2.model.Role;
import edu.uea.trabalho_final_APS_2.model.Status;
import edu.uea.trabalho_final_APS_2.repository.LivroRepository;
import edu.uea.trabalho_final_APS_2.repository.UsuarioRepository;

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

    public AdminInitializer(
            UsuarioRepository usuarioRepository,
            LivroRepository livroRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
        this.passwordEncoder = passwordEncoder;
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

        List<Livro> livros = List.of(
                criarLivro(
                        "O Senhor dos Anéis: A Sociedade do Anel",
                        "J.R.R. Tolkien",
                        "Fantasia",
                        576,
                        LocalDate.of(1954, 7, 28),
                        "Romance"
                ),
                criarLivro(
                        "Dom Casmurro - Edição Atualizada",
                        "Machado de Assis",
                        "Romance",
                        300,
                        LocalDate.of(1898, 12, 31),
                        "Literatura Brasileira"
                ),
                criarLivro(
                        "Dom Casmurro",
                        "Machado de Assis",
                        "Romance",
                        256,
                        LocalDate.of(1898, 12, 31),
                        "Literatura Brasileira"
                ),
                criarLivro(
                        "O Pequeno Príncipe",
                        "Antoine de Saint-Exupéry",
                        "Infantojuvenil",
                        96,
                        LocalDate.of(1943, 4, 6),
                        "Infantil"
                ),
                criarLivro(
                        "Harry Potter e a Pedra Filosofal",
                        "J.K. Rowling",
                        "Fantasia",
                        264,
                        LocalDate.of(1997, 6, 26),
                        "Romance"
                ),
                criarLivro(
                        "O Hobbit",
                        "J.R.R. Tolkien",
                        "Fantasia",
                        310,
                        LocalDate.of(1937, 9, 21),
                        "Romance"
                ),
                criarLivro(
                        "O Morro dos Ventos Uivantes",
                        "Emily Bronte",
                        "Romance",
                        416,
                        LocalDate.of(1847, 11, 30),
                        "Classico"
                ),
                criarLivro(
                        "Memórias Póstumas de Brás Cubas",
                        "Machado de Assis",
                        "Romance",
                        208,
                        LocalDate.of(1881, 3, 14),
                        "Literatura Brasileira"
                ),
                criarLivro(
                        "It A Coisa",
                        "Stephen King",
                        "Terror",
                        1138,
                        LocalDate.of(1986, 9, 14),
                        "Horror"
                ),
                criarLivro(
                        "O Nome do Vento",
                        "Patrick Rothfuss",
                        "Fantasia",
                        662,
                        LocalDate.of(2007, 3, 26),
                        "Fantasia"
                ),
                criarLivro(
                        "Jogos Vorazes",
                        "Suzanne Collins",
                        "Distopia",
                        400,
                        LocalDate.of(2008, 9, 13),
                        "Aventura"
                ),
                criarLivro(
                        "Clean Code",
                        "Robert C. Martin",
                        "Livro Técnico",
                        440,
                        LocalDate.of(2008, 3, 19),
                        "Programação"
                ),
                criarLivro(
                        "Entendendo Algoritmos",
                        "Aditya Y. Bhargava",
                        "Livro técnico",
                        256,
                        LocalDate.of(2016, 5, 12),
                        "Computação"
                )
        );

        livroRepository.saveAll(livros);

        System.out.println("Livros padrão cadastrados com sucesso.");
    }

    private Livro criarLivro(
            String titulo,
            String autor,
            String genero,
            int numPaginas,
            LocalDate anoDePublicacao,
            String categoria) {

        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setAutor(autor);
        livro.setGenero(genero);
        livro.setNumPaginas(numPaginas);
        livro.setAnoDePublicacao(anoDePublicacao);
        livro.setCategoria(categoria);
        livro.setStatus(Status.DISPONIVEL);

        return livro;
    }
}