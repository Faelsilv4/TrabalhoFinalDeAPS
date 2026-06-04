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
                                                "Romance",
                                                "https://covers.openlibrary.org/b/isbn/9780261103573-L.jpg"),

                

                                criarLivro(
                                                "Dom Casmurro",
                                                "Machado de Assis",
                                                "Romance",
                                                256,
                                                LocalDate.of(1898, 12, 31),
                                                "Literatura Brasileira",
                                                "https://covers.openlibrary.org/b/id/8231856-L.jpg"),

                                criarLivro(
                                                "O Pequeno Príncipe",
                                                "Antoine de Saint-Exupéry",
                                                "Infantojuvenil",
                                                96,
                                                LocalDate.of(1943, 4, 6),
                                                "Infantil",
                                                "https://m.media-amazon.com/images/I/51nNwwVSclL._SL1007_.jpg"),

                                criarLivro(
                                                "Harry Potter e a Pedra Filosofal",
                                                "J.K. Rowling",
                                                "Fantasia",
                                                264,
                                                LocalDate.of(1997, 6, 26),
                                                "Romance",
                                                "https://covers.openlibrary.org/b/isbn/9788532511010-L.jpg"),

                                criarLivro(
                                                "O Hobbit",
                                                "J.R.R. Tolkien",
                                                "Fantasia",
                                                310,
                                                LocalDate.of(1937, 9, 21),
                                                "Romance",
                                                "https://covers.openlibrary.org/b/isbn/9788595084742-L.jpg"),

                                criarLivro(
                                                "O Morro dos Ventos Uivantes",
                                                "Emily Bronte",
                                                "Romance",
                                                416,
                                                LocalDate.of(1847, 11, 30),
                                                "Classico",
                                                "https://m.media-amazon.com/images/I/9154Q7fv5UL._SL1500_.jpg"),

                                criarLivro(
                                                "Memórias Póstumas de Brás Cubas",
                                                "Machado de Assis",
                                                "Romance",
                                                208,
                                                LocalDate.of(1881, 3, 14),
                                                "Literatura Brasileira",
                                                "https://m.media-amazon.com/images/I/815u+SBDpJL._SL1500_.jpg"),

                                criarLivro(
                                                "It A Coisa",
                                                "Stephen King",
                                                "Terror",
                                                1138,
                                                LocalDate.of(1986, 9, 14),
                                                "Horror",
                                                "https://covers.openlibrary.org/b/isbn/9781501142970-L.jpg"),

                                criarLivro(
                                                "O Nome do Vento",
                                                "Patrick Rothfuss",
                                                "Fantasia",
                                                662,
                                                LocalDate.of(2007, 3, 26),
                                                "Fantasia",
                                                "https://m.media-amazon.com/images/I/81CGmkRG9GL._SL1500_.jpg"),

                                criarLivro(
                                                "Jogos Vorazes",
                                                "Suzanne Collins",
                                                "Distopia",
                                                400,
                                                LocalDate.of(2008, 9, 13),
                                                "Aventura",
                                                "https://m.media-amazon.com/images/I/71WOkspHbOL._SL1500_.jpg"),

                                criarLivro(
                                                "Clean Code",
                                                "Robert C. Martin",
                                                "Livro Técnico",
                                                440,
                                                LocalDate.of(2008, 3, 19),
                                                "Programação",
                                                "https://covers.openlibrary.org/b/isbn/9780132350884-L.jpg"),

                                criarLivro(
                                                "Entendendo Algoritmos",
                                                "Aditya Y. Bhargava",
                                                "Livro técnico",
                                                256,
                                                LocalDate.of(2016, 11, 1),
                                                "Computação",
                                                "https://m.media-amazon.com/images/I/71Vkg7GfPFL._SL1296_.jpg"));

                livroRepository.saveAll(livros);

                System.out.println("Livros padrão cadastrados com sucesso.");
        }

        private Livro criarLivro(
                        String titulo,
                        String autor,
                        String genero,
                        int numPaginas,
                        LocalDate anoDePublicacao,
                        String categoria,
                        String urlCapa) {

                Livro livro = new Livro();
                livro.setTitulo(titulo);
                livro.setAutor(autor);
                livro.setGenero(genero);
                livro.setNumPaginas(numPaginas);
                livro.setAnoDePublicacao(anoDePublicacao);
                livro.setCategoria(categoria);
                livro.setUrlCapa(urlCapa);
                livro.setStatus(Status.DISPONIVEL);

                return livro;
        }
}