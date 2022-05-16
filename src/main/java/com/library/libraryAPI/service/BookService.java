package com.library.libraryAPI.service;

import com.library.libraryAPI.api.DTO.BookDTO;
import com.library.libraryAPI.model.Entity.Book;

import java.util.Optional;

public interface BookService {
    Book save(Book book);

    Optional<Book> getById(Long id);

    void delete(Book book);

    Book update(Book book);
}
