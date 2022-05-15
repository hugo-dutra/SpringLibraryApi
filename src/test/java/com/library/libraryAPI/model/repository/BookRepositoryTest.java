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
        Book book = Book.builder().title("Aventuras").author("Fulano").isbn(isbn).build();
        testEntityManager.persist(book);
        //Execução
        boolean exists = bookRepository.existsByIsbn(isbn);
        //Verificação
        assertThat(exists).isTrue();
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


}
