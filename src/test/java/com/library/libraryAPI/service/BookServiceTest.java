package com.library.libraryAPI.service;

import com.library.libraryAPI.exception.BusinessException;
import com.library.libraryAPI.model.Entity.Book;
import com.library.libraryAPI.model.repository.BookRepository;
import com.library.libraryAPI.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
    BookService booksService;
    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp(){
        this.booksService = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookText() {
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(book))
                .thenReturn(Book.builder().id(1l).isbn("123").author("Fulano").title("As aventuras")
                        .build());

        Book savedBook = booksService.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
        assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
    }

    private Book createValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar salvar um livro com isbn duplicado")
    public void ShouldNotSaveABookWithDuplicatedISBN(){
        //Cenário
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
        //Execução
        Throwable exception = Assertions.catchThrowable(() -> booksService.save(book));
        //verificações.
        assertThat(exception).isInstanceOf(BusinessException.class).hasMessage("Isbn já cadastrado");
        Mockito.verify(repository,Mockito.never()).save(book);
    }


}
