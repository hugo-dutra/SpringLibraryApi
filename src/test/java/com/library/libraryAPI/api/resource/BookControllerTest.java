package com.library.libraryAPI.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.libraryAPI.api.DTO.BookDTO;
import com.library.libraryAPI.exception.BusinessException;
import com.library.libraryAPI.model.Entity.Book;
import com.library.libraryAPI.service.BookService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookService bookService;

    @Test
    @DisplayName("Create a new book")
    public void createBookTest() throws Exception {
        BookDTO bookDTO = createNewBook();
        Book savedBook = Book.builder().id(10L).author("Autor").title("Meu Livro").isbn("1213212").build();

        String json = new ObjectMapper().writeValueAsString(bookDTO);
        BDDMockito.given(bookService.save(Mockito.any(Book.class))).willReturn(savedBook);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(10))
                .andExpect(jsonPath("title").value(savedBook.getTitle()))
                .andExpect(jsonPath("author").value(savedBook.getAuthor()))
                .andExpect(jsonPath("isbn").value(savedBook.getIsbn()));
    }

    @Test
    @DisplayName("Deve lança erro de valicação quando não houver dados suficientes pra cração do livro")
    public void createInvalidBookTest() throws Exception{
        String json = new ObjectMapper().writeValueAsString(new BookDTO());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors",hasSize(3)) );
    }

    @Test
    @DisplayName("Erro ao tentar cadastrar um livro com isbn já utilizado por outro")
    public void createBookWithDuplicatedIsbn() throws Exception{
        BookDTO newBook = createNewBook();
        String MSG_ERROR = "Isbn já cadastrado";
        BDDMockito.given(bookService.save(Mockito.any(Book.class)))
                .willThrow(new BusinessException(MSG_ERROR));
        String json = new ObjectMapper().writeValueAsString(newBook);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors",hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(MSG_ERROR));
    }

    private BookDTO createNewBook() {
        return BookDTO.builder().author("Autor").title("Meu Livro").isbn("111").build();
    }
}
