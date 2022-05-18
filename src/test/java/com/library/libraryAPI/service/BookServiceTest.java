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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

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

    @Test
    @DisplayName("Deve obter um livro por id")
    public void getByIdTest(){
        //Cenário
        Long id = 1L;
        Book book = createValidBook();
        book.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));
        //Execução
        Optional<Book> foundBook = booksService.getById(id);
        //Verificação
        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(id);
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("Deve retornar vazio ao obter um livro por id quando ele não existe na base")
    public void bookNotFoundByIdTest(){
        //Cenário
        Long id = 1L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        //Execução
        Optional<Book> book = booksService.getById(id);
        //Verificação
        assertThat(book.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve deletar um livro cujo id foi informado")
    public void deleteBookByTest(){
        //Cenário
        Long id = 1L;
        Book book = Book.builder().id(id).build();
        //Execução
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(()->booksService.delete(book)); ;
        //Verificação
        Mockito.verify(repository,Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar deletar livro inexistente")
    public void deleteInvalidBookTest(){
        //Cenário
        Book book = new Book();
        //Execução
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,()->booksService.delete(book));
        //Verificação
        Mockito.verify(repository,Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Deve retornar um erro ao tentar atualizar um livro inválido")
    public void updateInvalidBookByTest(){
        Book book = new Book();
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,()->booksService.update(book));
        Mockito.verify(repository,Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBookByTest(){
        Long id = 1L;
        Book updatingBook = Book.builder().id(id).build();
        Book updatedBook = createValidBook();
        updatingBook.setId(id);

        Mockito.when(repository.save(updatingBook)).thenReturn(updatedBook);

        Book book = booksService.update(updatingBook);

        assertThat(book.getId()).isEqualTo(updatedBook.getId());
        assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());
        assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());
    }

    @Test
    @DisplayName("Deve filtrar livros pela propriedade")
    public void findBookTest(){
        //Cenário
        Book book = createValidBook();
        PageRequest pageRequest = PageRequest.of(0,10);
        Page<Book> page = new PageImpl<Book>(Arrays.asList(book), PageRequest.of(0,10),1);
        Mockito.when(repository.findAll(Mockito.any(Example.class),Mockito.any(PageRequest.class)))
                .thenReturn(page);
        //Execução
        Page<Book> result = booksService.find(book, pageRequest);
        //Verificações
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isNotNull();
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }





}
