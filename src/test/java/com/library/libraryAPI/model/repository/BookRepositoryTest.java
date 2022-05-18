package com.library.libraryAPI.model.repository;

import com.library.libraryAPI.model.Entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {
    @Autowired
    TestEntityManager testEntityManager;
    @Autowired
    BookRepository bookRepository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro com o isbn informado")
    public void returnTrueWhenIsbnExists(){
        //Cenário
        String isbn = "123";
        Book book = createNewBook(isbn);
        testEntityManager.persist(book);
        //Execução
        boolean exists = bookRepository.existsByIsbn(isbn);
        //Verificação
        assertThat(exists).isTrue();
    }

    private Book createNewBook(String isbn) {
        return Book.builder().title("Aventuras").author("Fulano").isbn(isbn).build();
    }

    @Test
    @DisplayName("Deve retornar false quando não existir um livro com o isbn informado")
    public void returnTrueWhenIsbnDoesntExists(){
        //Cenário
        String isbn = "123";
        //Execução
        boolean exists = bookRepository.existsByIsbn(isbn);
        //Verificação
        assertThat(exists).isFalse();
    }
    @Test
    @DisplayName("Deve obter um livro por id.")
    public void findByIdTest(){
        //cenário
        Book book = createNewBook("123");
        testEntityManager.persist(book);
        //execução
        Optional<Book> foundBook = bookRepository.findById(book.getId());
        //verificação
        assertThat(foundBook.isPresent()).isTrue();
    }
    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        Book book = createNewBook("123");
        Book savedBook = bookRepository.save(book);
        assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest(){
        Book book = createNewBook("123");
        testEntityManager.persist(book);
        Book foundBook = testEntityManager.find(Book.class, book.getId());
        bookRepository.delete(foundBook);
        Book deletedBook = testEntityManager.find(Book.class, book.getId());
        assertThat(deletedBook).isNull();
    }



}
